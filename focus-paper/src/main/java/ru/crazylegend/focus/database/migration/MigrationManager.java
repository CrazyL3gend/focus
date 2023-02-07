package ru.crazylegend.focus.database.migration;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import ru.crazylegend.focus.database.DataRegistryManager;
import ru.crazylegend.focus.database.Database;
import ru.crazylegend.focus.database.credential.DatabaseCredentials;
import ru.crazylegend.focus.database.migration.annotation.ActualSchemaVersion;
import ru.crazylegend.focus.database.migration.config.MigrationParser;
import ru.crazylegend.focus.database.migration.exception.DataConvertationException;
import ru.crazylegend.focus.database.migration.exception.MigrationParseException;
import ru.crazylegend.focus.database.migration.exception.MigrationResolveException;
import ru.crazylegend.focus.database.migration.exception.MigrationRunException;
import ru.crazylegend.focus.database.migration.runtime.MigrationDataConverter;
import ru.crazylegend.focus.database.migration.runtime.MigrationRunner;
import ru.crazylegend.focus.database.migration.runtime.WrappedDataConverter;
import ru.crazylegend.focus.database.migration.schema.DatabaseSchemaAnalyzer;
import ru.crazylegend.focus.database.migration.schema.TableStructuresAnalyzer;
import ru.crazylegend.focus.util.tool.Validate;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MigrationManager {

    public static final String SCHEMA_VERSION_KEY_FORMAT = "%s_schema_version";
    public static final int DEFAULT_SCHEMA_VERSION = 1;

    private static final Comparator<Migration> MIGRATION_COMPARATOR;

    static {
        Comparator<OptionalInt> ORDER_COMPARATOR = (order1, order2) -> {
            if (order1.isPresent() && order2.isPresent())
                return Integer.compare(order1.getAsInt(), order2.getAsInt());

            if (order1.isPresent())
                return 1;

            if (order2.isPresent())
                return -1;

            return 0;
        };

        MIGRATION_COMPARATOR = Comparator.comparingInt(Migration::getVersion)   // by a schema version
                .thenComparing(Migration::getOrder, ORDER_COMPARATOR)           // by an order position
                .thenComparing(Migration::getId, String::compareToIgnoreCase);  // by an ID
    }

    private final Plugin plugin;
    private final Database database;
    private final DataRegistryManager dataRegistryManager;
    private final Map<String, MigrationDataConverter<?, ?>> dataConverters;
    private final SortedMap<Integer, DatabaseSchemaAnalyzer> schemaAnalyzers;

    private Integer currentSchemaVersion;
    private Integer actualSchemaVersion;
    private String migrationsPathRoot = MigrationParser.DEFAULT_MIGRATIONS_PATH_ROOT;

    public MigrationManager(@NotNull Plugin plugin, @NotNull Database database) throws SQLException {
        this.plugin = plugin;
        this.database = database;
        this.dataRegistryManager = new DataRegistryManager(plugin, database.getBootstrapConnection());
        this.dataConverters = new LinkedHashMap<>();
        this.schemaAnalyzers = new TreeMap<>(Comparator.naturalOrder());
    }

    public boolean analyzeDatabaseSchema() {
        DatabaseCredentials credentials = database.getCredentials();
        ConnectionSource bootstrapConnection = database.getBootstrapConnection();

        resolveActualSchemaVersion();

        boolean hasLastRevision = false;
        boolean someonePassed = false;

        Iterator<Integer> iterator = schemaAnalyzers.keySet().iterator();
        while (iterator.hasNext()) {
            int schemaVersion = iterator.next();
            DatabaseSchemaAnalyzer analyzer = schemaAnalyzers.get(schemaVersion);

            try {
                if (analyzer.analyze(bootstrapConnection, schemaVersion)) {
                    this.currentSchemaVersion = schemaVersion;
                    someonePassed = true;

                    if (!iterator.hasNext()) {
                        hasLastRevision = true;
                    }

                    if (analyzer instanceof TableStructuresAnalyzer) {
                        database.unregisterTables(((TableStructuresAnalyzer) analyzer).getTableClasses());
                    }
                }
            } catch (SQLException ignored) {
            }
        }

        if (!someonePassed) {
            this.currentSchemaVersion = actualSchemaVersion;
            hasLastRevision = true;
        }

        if (currentSchemaVersion != null) {
            updateCurrentSchemaVersion(currentSchemaVersion);
            if (Objects.equals(currentSchemaVersion, actualSchemaVersion)) {
                printInfo("Database schema is up to date.");
                return true;
            } else {
                printInfo("Detected database schema version: %d.", currentSchemaVersion);
            }
        }

        return hasLastRevision;
    }

    public void registerDataConverter(@NotNull String migrationPath, @NotNull MigrationDataConverter<?, ?> dataConverter) {
        Validate.notEmpty(migrationPath, "migrationPath");
        Validate.notNull(dataConverter, "dataConverter");

        InputStream migrationResource = plugin.getClass().getResourceAsStream(migrationPath);
        if (migrationResource == null)
            throw new IllegalArgumentException(String.format("Migration '%s' isn't provided by %s!", migrationPath, plugin));

        dataConverters.put(migrationPath, dataConverter);
    }

    public void registerDataConverter(@NotNull WrappedDataConverter<?, ?> wrappedDataConverter) {
        Validate.notNull(wrappedDataConverter, "wrappedDataConverter");
        String migrationPath = wrappedDataConverter.getFullMigrationPath(migrationsPathRoot);
        MigrationDataConverter<?, ?> dataConverter = wrappedDataConverter.getDataConverter();
        registerDataConverter(migrationPath, dataConverter);
    }

    public void registerSchemaAnalyzer(int schemaVersion, @NotNull DatabaseSchemaAnalyzer schemaAnalyzer) {
        Validate.notNull(schemaAnalyzer, "schemaAnalyzer");
        schemaAnalyzers.put(schemaVersion, schemaAnalyzer);
    }

    public void setActualSchemaVersion(int actualSchemaVersion) {
        Validate.isTrue(actualSchemaVersion >= 0, "Current schema version cannot be negative!");
        this.actualSchemaVersion = actualSchemaVersion;
    }

    public void setMigrationsPathRoot(@NotNull String migrationsPathRoot) {
        Validate.notNull(migrationsPathRoot, "migrationsPathRoot");
        this.migrationsPathRoot = migrationsPathRoot;
    }

    public boolean runMigrations() throws MigrationResolveException, MigrationParseException, MigrationRunException, DataConvertationException, SQLException {
        OptionalInt wrappedActualSchemaVersion = resolveActualSchemaVersion();
        if (!wrappedActualSchemaVersion.isPresent())
            return false;

        int actualSchemaVersion = wrappedActualSchemaVersion.getAsInt();
        int currentSchemaVersion = getCurrentSchemaVersion();

        if (actualSchemaVersion == currentSchemaVersion) {
            printInfo("Database schema is up to date.");
            return true;
        }

        if (actualSchemaVersion < currentSchemaVersion) {
            reportAsError("ABNORMALLY! The actual schema version (%d) is lower than a current version (%d)!", actualSchemaVersion, currentSchemaVersion);
            return true;
        }

        // --- running migrations
        MigrationParser migrationParser = new MigrationParser(plugin, migrationsPathRoot);
        migrationParser.findMigrationConfigs();

        Map<String, Migration> foundMigrations = migrationParser.getMigrations();
        Set<Migration> sortedMigrations = foundMigrations.values().stream()
                .sorted(MIGRATION_COMPARATOR)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        int finishedMigrationsCounter = 0;
        for (int version = currentSchemaVersion; version < actualSchemaVersion; version++) {
            printInfo("> Starting database schema migration from v%d to v%d...", version, version + 1);

            for (Migration migration : sortedMigrations) {
                if (migration.getVersion() == version + 1) {
                    boolean isSuccess = runMigration(migration);

                    if (isSuccess) {
                        printInfo("  > Migration '%s' has been finished!", migration.getName());
                        finishedMigrationsCounter++;
                    } else {
                        reportAsError("  > Migration '%s' has been failed!", migration.getName());
                        return false;
                    }
                }
            }

            updateCurrentSchemaVersion(version + 1);

            printInfo("> The database schema has been migrated from v%d to v%d.", version, version + 1);
        }

        printInfo(
                "The database schema has been migrated from v%d to v%d with %d migration(s).",
                currentSchemaVersion, actualSchemaVersion, finishedMigrationsCounter
        );
        return true;
    }

    @SuppressWarnings("unchecked")
    private <OLD, NEW> boolean runMigration(@NotNull Migration migration) throws MigrationRunException, DataConvertationException, SQLException {
        printInfo("  > Processing migration '%s'...", migration.getName());

        MigrationDataConverter<OLD, NEW> dataConverter = (MigrationDataConverter<OLD, NEW>) dataConverters.get(migration.getResourcePath());
        Optional<String> sourceTableName = migration.getSourceTableName();
        Optional<String> destinationTableName = migration.getDestinationTableName();

        Class<OLD> oldDataType;
        Class<NEW> newDataType = null;
        List<NEW> newDataEntries = null;

        ConnectionSource bootstrapConnection = database.getBootstrapConnection();
        if (dataConverter != null) {
            if (!sourceTableName.isPresent())
                throw new MigrationRunException(migration, "source table name must be specified via 'source' property!");

            if (!destinationTableName.isPresent())
                throw new MigrationRunException(migration, "destination table name must be specified via 'destination' property!");

            Type[] genericInterfaces = dataConverter.getClass().getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                ParameterizedType parameterizedInterface = (ParameterizedType) genericInterface;
                if (parameterizedInterface.getRawType() == MigrationDataConverter.class) {
                    oldDataType = (Class<OLD>) parameterizedInterface.getActualTypeArguments()[0];
                    newDataType = (Class<NEW>) parameterizedInterface.getActualTypeArguments()[1];

                    List<OLD> oldDataEntries = extractOldData(migration, oldDataType, sourceTableName.get());
                    newDataEntries = convertDataEntries(oldDataEntries, dataConverter, oldDataType, newDataType);
                }
            }
        }

        DatabaseConnection migrationConnection = bootstrapConnection.getReadWriteConnection(null);
        boolean isSuccess = new MigrationRunner(migration, migrationConnection).runStatements();

        if (isSuccess) {
            bootstrapConnection.releaseConnection(migrationConnection);
        } else {
            return false;
        }

        if (newDataEntries != null && !newDataEntries.isEmpty()) {
            importNewData(migration, newDataEntries, newDataType, destinationTableName.get());
        }

        return true;
    }

    private <OLD> @NotNull @UnmodifiableView List<OLD> extractOldData(
            @NotNull Migration migration,
            @NotNull Class<OLD> oldDataType,
            @NotNull String sourceTableName
    ) throws SQLException {
        printInfo("    > Extracting old data from table '%s'...", sourceTableName);

        ConnectionSource bootstrapConnection = database.getBootstrapConnection();
        Dao<OLD, ?> oldDataDao = DaoManager.createDao(bootstrapConnection, oldDataType);
        return oldDataDao.queryForAll();
    }

    private <OLD, NEW> @NotNull @UnmodifiableView List<NEW> convertDataEntries(
            @NotNull List<OLD> oldDataEntries,
            @NotNull MigrationDataConverter<OLD, NEW> converter,
            @NotNull Class<OLD> oldDataType,
            @NotNull Class<NEW> newDataType
    ) throws DataConvertationException {
        if (oldDataEntries.isEmpty())
            return Collections.emptyList();

        String oldType = oldDataType.getSimpleName();
        String newType = newDataType.getSimpleName();

        printInfo("    > Converting %d old entries to new format (%s -> %s)...", oldDataEntries.size(), oldType, newType);

        try {
            List<NEW> newDataEntries = new ArrayList<>();

            for (OLD oldDataEntry : oldDataEntries) {
                NEW newDataEntry = converter.convert(oldDataEntry);
                newDataEntries.add(newDataEntry);
            }

            return newDataEntries;
        } catch (Throwable ex) {
            throw new DataConvertationException(oldDataType, newDataType, ex);
        }
    }

    private <NEW> int importNewData(
            @NotNull Migration migration,
            @NotNull List<NEW> newDataEntries,
            @NotNull Class<NEW> newDataType,
            @NotNull String destinationTableName
    ) throws SQLException {
        printInfo("    > Importing new data into table '%s'...", destinationTableName);

        ConnectionSource bootstrapConnection = database.getBootstrapConnection();

        TableUtils.createTableIfNotExists(bootstrapConnection, newDataType);
        Dao<NEW, ?> oldDataDao = DaoManager.createDao(bootstrapConnection, newDataType);
        return oldDataDao.create(newDataEntries);
    }

    public int getCurrentSchemaVersion() {
        return getCurrentSchemaVersion(DEFAULT_SCHEMA_VERSION);
    }

    public int getCurrentSchemaVersion(int defaultVersion) {
        if (currentSchemaVersion != null)
            return currentSchemaVersion;

        String key = String.format(SCHEMA_VERSION_KEY_FORMAT, plugin.getName().toLowerCase());
        return dataRegistryManager.getEntryOrDefault(key, defaultVersion).join()
                .getValueAsInt()
                .orElseThrow(() -> new IllegalArgumentException("Database has returned a non-digital schema version!"));
    }

    private void updateCurrentSchemaVersion(int version) {
        String key = String.format(SCHEMA_VERSION_KEY_FORMAT, plugin.getName().toLowerCase());
        dataRegistryManager.saveEntry(key, version).join();
    }

    public @NotNull OptionalInt resolveActualSchemaVersion() {
        if (actualSchemaVersion != null)
            return OptionalInt.of(actualSchemaVersion);

        ActualSchemaVersion typeAnnotation = plugin.getClass().getAnnotation(ActualSchemaVersion.class);
        if (typeAnnotation != null) {
            int value = typeAnnotation.value();
            Validate.isTrue(value >= 0, "Current schema version cannot be negative!");
            this.actualSchemaVersion = value;
            return OptionalInt.of(value);
        }

        Field[] publicFields = plugin.getClass().getFields();
        for (Field publicField : publicFields) {
            ActualSchemaVersion fieldAnnotation = publicField.getAnnotation(ActualSchemaVersion.class);
            if (fieldAnnotation != null) {
                if (fieldAnnotation.value() == -1) {
                    try {
                        publicField.getInt(plugin);
                    } catch (IllegalAccessException ex) {
                        reportAsError(
                                "Actual schema version determining field '%s' from %s isn't accessible!",
                                publicField.getName(), plugin.getClass().getName()
                        );
                    } catch (IllegalArgumentException ex) {
                        reportAsError(
                                "Couldn't get an int value from actual schema version determining field '%s' in %s!",
                                publicField.getName(), plugin.getClass().getName()
                        );
                    }
                    return OptionalInt.empty();
                } else {
                    int value = fieldAnnotation.value();
                    Validate.isTrue(value >= 0, "Current schema version cannot be negative!");
                    this.actualSchemaVersion = value;
                    return OptionalInt.of(value);
                }
            }
        }

        reportAsError("Couldn't determine an actual database schema version for this plugin!");
        reportAsError(
                "You can specify that manually using the Database#setActualSchemaVersion " +
                        "chained method or using the @ActualSchemaVersion annotation above the plugin " +
                        "main class or above one of the public (static) int field."
        );

        return OptionalInt.empty();
    }

    private void printInfo(@NotNull String message, Object... args) {
        plugin.getLogger().info(String.format(message, args));
    }

    private void reportAsError(@NotNull String message, Object... args) {
        plugin.getLogger().severe(String.format(message, args));
    }

}
