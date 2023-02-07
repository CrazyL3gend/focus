package ru.crazylegend.focus.database.migration.config;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.database.migration.Migration;
import ru.crazylegend.focus.database.migration.MigrationTarget;
import ru.crazylegend.focus.database.migration.SimpleMigration;
import ru.crazylegend.focus.database.migration.exception.MigrationParseException;

import java.util.*;
import java.util.function.Consumer;

public class MigrationConfiguration {

    public static final String MIGRATION_HEADER = "Migration";
    public static final String STATEMENTS_HEADER = "Statements";

    public static final String MIGRATION_NAME_KEY = "name";
    public static final String MIGRATION_DESCRIPTION_KEY = "description";
    public static final String MIGRATION_VERSION_KEY = "version";
    public static final String MIGRATION_ORDER_KEY = "order";
    public static final String MIGRATION_TARGET_KEY = "target";
    public static final String MIGRATION_SOURCE_TABLE_KEY = "source";
    public static final String MIGRATION_DESTINATION_TABLE_KEY = "destination";

    private final Plugin plugin;
    private final String resourcePath;
    private final String id;

    private final Map<String, MigrationConfigurationSection> sections;
    private MigrationConfigurationSection currentSection;

    public MigrationConfiguration(@NotNull Plugin plugin, @NotNull String resourcePath, @NotNull String id) throws MigrationParseException {
        validateNotNull(plugin, "'plugin' cannot be null!");
        validateNotEmpty(id, "'id' cannot be null or empty!");
        validateNotEmpty(resourcePath, "'resourcePath' cannot be null or empty!");

        this.plugin = plugin;
        this.resourcePath = resourcePath;
        this.id = id;
        this.sections = new LinkedHashMap<>();
    }

    public void addContentLineForCurrentSection(@NotNull String line) {
        insertIntoCurrentSection(section -> section.addContentLine(line));
    }

    public void addParameterForCurrentSection(@NotNull String key, @NotNull Object value) {
        insertIntoCurrentSection(section -> section.addParameter(key, value));
    }

    public void changeCurrentSection(@NotNull MigrationConfigurationSection section) {
        this.sections.put(section.getHeaderId(), section);
        this.currentSection = section;
    }

    public @NotNull Optional<MigrationConfigurationSection> getSection(@NotNull String headerId) {
        return Optional.ofNullable(sections.get(headerId));
    }

    public @NotNull Migration makeInstance() throws MigrationParseException {
        // structure validation
        Optional<MigrationConfigurationSection> migrationSection = getSection(MIGRATION_HEADER);
        validateWithCondition(migrationSection.isPresent(), "there are no migration properties section!");

        Optional<MigrationConfigurationSection> statementsSection = getSection(STATEMENTS_HEADER);
        validateWithCondition(statementsSection.isPresent(), "there are no SQL statements list!");

        // properties extraction
        String name = migrationSection.get().getValue(MIGRATION_NAME_KEY).orElse(id);
        String description = migrationSection.get().getValue(MIGRATION_DESCRIPTION_KEY).orElse(null);
        int version = migrationSection.get().getValueAsInt(MIGRATION_VERSION_KEY).orElse(0);
        Integer order = migrationSection.get().getValue(MIGRATION_ORDER_KEY, Integer::parseInt, i -> i, () -> null);
        String targetRaw = migrationSection.get().getValue(MIGRATION_TARGET_KEY).orElse("");

        // data converter specification
        String sourceTableName = migrationSection.get().getValue(MIGRATION_SOURCE_TABLE_KEY).orElse(null);
        String destinationTableName = migrationSection.get().getValue(MIGRATION_DESTINATION_TABLE_KEY).orElse(null);

        validateWithCondition(version > 0, "schema version must be positive!");

        Set<MigrationTarget> targets = MigrationTarget.getByKeys(targetRaw);
        List<String> statements = statementsSection.get().getContent();

        return new SimpleMigration(plugin, id, resourcePath, targets, version, order, name, description, sourceTableName, destinationTableName, statements);
    }

    private void insertIntoCurrentSection(@NotNull Consumer<MigrationConfigurationSection> insertion) {
        if (currentSection != null)
            insertion.accept(currentSection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MigrationConfiguration that = (MigrationConfiguration) o;
        return Objects.equals(plugin, that.plugin) &&
                Objects.equals(resourcePath, that.resourcePath) &&
                Objects.equals(id, that.id) &&
                Objects.equals(sections, that.sections) &&
                Objects.equals(currentSection, that.currentSection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin, resourcePath, id, sections, currentSection);
    }

    @Override
    public @NotNull String toString() {
        return "MigrationConfiguration{" +
                "plugin=" + plugin +
                ", resourcePath='" + resourcePath + '\'' +
                ", id='" + id + '\'' +
                ", sections=" + sections +
                ", currentSection=" + currentSection +
                '}';
    }

    private void validateNotNull(@Nullable Object object, @NotNull String message) throws MigrationParseException {
        if (object == null)
            throw new MigrationParseException(resourcePath, message);
    }

    private void validateNotEmpty(@Nullable String object, @NotNull String message) throws MigrationParseException {
        if (object == null || object.isEmpty())
            throw new MigrationParseException(resourcePath, message);
    }

    private void validateWithCondition(boolean condition, @NotNull String message) throws MigrationParseException {
        if (!condition)
            throw new MigrationParseException(resourcePath, message);
    }

}
