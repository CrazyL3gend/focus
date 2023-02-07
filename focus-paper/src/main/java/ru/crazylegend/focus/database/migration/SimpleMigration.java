package ru.crazylegend.focus.database.migration;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public final class SimpleMigration implements Migration {

    private final @NotNull Plugin plugin;
    private final @NotNull String id;
    private final @NotNull String resourcePath;
    private final @NotNull Set<MigrationTarget> targets;
    private final int version;
    private final @Nullable Integer order;
    private final @NotNull String name;
    private final @Nullable String description;
    private final @Nullable String sourceTableName;
    private final @Nullable String destinationTableName;
    private final @NotNull List<String> statements;

    public SimpleMigration(
            @NotNull Plugin plugin,
            @NotNull String id,
            @NotNull String resourcePath,
            @NotNull Set<MigrationTarget> targets,
            int version,
            @Nullable Integer order,
            @Nullable String name,
            @Nullable String description,
            @Nullable String sourceTableName,
            @Nullable String destinationTableName,
            @Nullable List<String> statements
    ) {
        this.plugin = plugin;
        this.id = id;
        this.resourcePath = resourcePath;
        this.targets = targets != null ? targets : Collections.emptySet();
        this.version = Math.max(version, 1);
        this.order = order;
        this.name = name != null ? name : id;
        this.description = description;
        this.sourceTableName = sourceTableName;
        this.destinationTableName = destinationTableName;
        this.statements = statements != null ? new ArrayList<>(statements) : Collections.emptyList();
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull String getResourcePath() {
        return resourcePath;
    }

    @Override
    public @NotNull Set<MigrationTarget> getTargets() {
        return targets;
    }

    @Override
    public boolean isApplicableFor(@NotNull MigrationTarget target) {
        if (targets.isEmpty())
            return true;

        if (targets.contains(MigrationTarget.ALL))
            return true;

        return targets.contains(target);
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public @NotNull OptionalInt getOrder() {
        return order != null ? OptionalInt.of(order) : OptionalInt.empty();
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public @NotNull Optional<String> getSourceTableName() {
        return Optional.ofNullable(sourceTableName);
    }

    @Override
    public @NotNull Optional<String> getDestinationTableName() {
        return Optional.ofNullable(destinationTableName);
    }

    @Override
    public @NotNull @UnmodifiableView List<String> getSQLStatements() {
        return Collections.unmodifiableList(statements);
    }

    @Override
    public boolean hasSQLStatements() {
        return !statements.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleMigration that = (SimpleMigration) o;
        return version == that.version &&
                Objects.equals(plugin, that.plugin) &&
                Objects.equals(id, that.id) &&
                Objects.equals(resourcePath, that.resourcePath) &&
                Objects.equals(targets, that.targets) &&
                Objects.equals(order, that.order) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(sourceTableName, that.sourceTableName) &&
                Objects.equals(destinationTableName, that.destinationTableName) &&
                Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                plugin, id, resourcePath, targets, version, order, name,
                description, sourceTableName, destinationTableName, statements
        );
    }

    @Override
    public @NotNull String toString() {
        return "SimpleMigration{" +
                "plugin=" + plugin +
                ", id='" + id + '\'' +
                ", resourcePath='" + resourcePath + '\'' +
                ", targets=" + targets +
                ", version=" + version +
                ", order=" + order +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sourceTableName='" + sourceTableName + '\'' +
                ", destinationTableName='" + destinationTableName + '\'' +
                ", statements=" + statements +
                '}';
    }

}
