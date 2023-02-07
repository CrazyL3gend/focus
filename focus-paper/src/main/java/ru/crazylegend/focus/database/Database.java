package ru.crazylegend.focus.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.configuration.Configuration;
import ru.crazylegend.focus.database.credential.AuthDatabaseCredentials;
import ru.crazylegend.focus.database.credential.DatabaseCredentials;
import ru.crazylegend.focus.database.exception.*;
import ru.crazylegend.focus.database.migration.MigrationManager;
import ru.crazylegend.focus.database.migration.exception.AbstractMigrationException;
import ru.crazylegend.focus.database.migration.runtime.MigrationDataConverter;
import ru.crazylegend.focus.database.migration.runtime.WrappedDataConverter;
import ru.crazylegend.focus.database.migration.schema.DatabaseSchemaAnalyzer;
import ru.crazylegend.focus.util.tool.Validate;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class Database {

    private final Plugin plugin;
    private final Set<Class<?>> registeredTables;

    private final DatabaseCredentials credentials;
    private final ConnectionSource bootstrapConnection;
    private final MigrationManager migrationManager;

    public Database(@NotNull Plugin plugin, @NotNull Configuration config) throws
            CredentialsParseException,
            DatabaseBootstrapException,
            DriverNotFoundException,
            DriverLoadException,
            SQLException,
            UnknownDatabaseTypeException {
        this(plugin, config.getSection("database"));
    }

    public Database(@NotNull Plugin plugin, @NotNull ConfigurationSection databaseSection) throws
            CredentialsParseException,
            DatabaseBootstrapException,
            DriverNotFoundException,
            DriverLoadException,
            SQLException,
            UnknownDatabaseTypeException {
        this.plugin = plugin;
        this.registeredTables = new LinkedHashSet<>();

        // check is database section exists
        if (databaseSection == null)
            throw new DatabaseBootstrapException("'database' section was not found in the main config!");

        String typeId = databaseSection.getString("type");
        DatabaseType databaseType = DatabaseType.getById(typeId);

        // check is database connection configuration exists
        ConfigurationSection typeSection = databaseSection.getConfigurationSection(typeId);
        if (typeSection == null)
            throw new DatabaseBootstrapException("'database." + typeId + "' section was not found in the main config!");

        this.credentials = DatabaseCredentials.parse(typeSection, databaseType);

        // loading the database JDBC driver
        credentials.loadDriver(plugin);

        // trying to connect
        this.bootstrapConnection = establishConnection();

        // migrations manager instance creation
        this.migrationManager = new MigrationManager(plugin, this);
    }

    public @NotNull Database complete() throws SQLException {
        try {
            return complete(false);
        } catch (AbstractMigrationException ignored) {
            return this;
        }
    }

    public @NotNull Database complete(boolean performMigrations) throws AbstractMigrationException, SQLException {
        Validate.notNull(bootstrapConnection, "bootstrapConnection");

        // analyzing database schema
        boolean hasLastRevision = migrationManager.analyzeDatabaseSchema();
        boolean migrationsRequired = performMigrations && !hasLastRevision;

        // creating all registered tables
        createTables(registeredTables);

        // perform migrations
        if (migrationsRequired)
            migrationManager.runMigrations();

        // closing the bootstrap connection
        bootstrapConnection.closeQuietly();
        return this;
    }

    public @NotNull ConnectionSource establishConnection() throws SQLException {
        Validate.notNull(credentials, "credentials");

        String url = credentials.getConnectionUrl(plugin);
        if (!credentials.isAuthRequired())
            return new JdbcConnectionSource(url);

        if (credentials instanceof AuthDatabaseCredentials) {
            AuthDatabaseCredentials authCredentials = (AuthDatabaseCredentials) credentials;
            String username = authCredentials.getUsername();
            String password = authCredentials.getPassword();
            return new JdbcConnectionSource(url, username, password);
        }

        throw new IllegalArgumentException(
                "Credentials auth is required, but it is not an " +
                        "instance of the AuthDatabaseCredentials!"
        );
    }

    public @NotNull Database setActualSchemaVersion(int version) {
        migrationManager.setActualSchemaVersion(version);
        return this;
    }

    public @NotNull Database setMigrationsPathRoot(@NotNull String migrationsPathRoot) {
        migrationManager.setMigrationsPathRoot(migrationsPathRoot);
        return this;
    }

    public @NotNull Database registerDataConverter(@NotNull String migrationPath, @NotNull MigrationDataConverter<?, ?> dataConverter) {
        migrationManager.registerDataConverter(migrationPath, dataConverter);
        return this;
    }

    public @NotNull Database registerDataConverter(@NotNull WrappedDataConverter<?, ?> wrappedDataConverter) {
        migrationManager.registerDataConverter(wrappedDataConverter);
        return this;
    }

    public @NotNull Database registerSchemaAnalyzer(int schemaVersion, @NotNull DatabaseSchemaAnalyzer schemaAnalyzer) {
        migrationManager.registerSchemaAnalyzer(schemaVersion, schemaAnalyzer);
        return this;
    }

    public @NotNull Database registerTable(@NotNull Class<?> tableClass) {
        Validate.notNull(tableClass, "tableClass");
        registeredTables.add(tableClass);
        return this;
    }

    public @NotNull Database registerTables(@NotNull Class<?>... tableClasses) {
        Validate.notEmpty(tableClasses, "tableClasses");
        return registerTables(Arrays.asList(tableClasses));
    }

    public @NotNull Database registerTables(@NotNull Collection<Class<?>> tableClasses) {
        Validate.notEmpty(tableClasses, "tableClasses");
        registeredTables.addAll(tableClasses);
        return this;
    }

    public @NotNull Database unregisterTable(@NotNull Class<?> tableClass) {
        Validate.notNull(tableClass, "tableClass");
        registeredTables.remove(tableClass);
        return this;
    }

    public @NotNull Database unregisterTables(@NotNull Collection<Class<?>> tableClasses) {
        Validate.notEmpty(tableClasses, "tableClasses");
        registeredTables.removeAll(tableClasses);
        return this;
    }

    public @NotNull Database createTable(@NotNull Class<?> daoClass) throws SQLException {
        Validate.notNull(daoClass, "daoClass");
        Validate.notNull(bootstrapConnection, "bootstrapConnection");

        TableUtils.createTableIfNotExists(bootstrapConnection, daoClass);
        return this;
    }

    /**
     * @deprecated Use {@link #registerTable(Class)} instead.
     */
    @Deprecated
    public @NotNull Database createTables(@NotNull Class<?>... daoClasses) throws SQLException {
        Validate.notNull(daoClasses, "daoClasses");
        return createTables(Arrays.asList(daoClasses));
    }

    /**
     * @deprecated Use {@link #registerTables(Collection)} instead.
     */
    @Deprecated
    public @NotNull Database createTables(@NotNull Collection<Class<?>> daoClasses) throws SQLException {
        Validate.notNull(daoClasses, "daoClasses");
        Validate.notNull(bootstrapConnection, "bootstrapConnection");

        for (Class<?> daoClass : daoClasses)
            TableUtils.createTableIfNotExists(bootstrapConnection, daoClass);

        return this;
    }

}