package ru.crazylegend.focus.database.migration;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public interface Migration {

    @NotNull Plugin getPlugin();

    @NotNull String getId();

    @NotNull String getResourcePath();

    @NotNull Set<MigrationTarget> getTargets();

    boolean isApplicableFor(@NotNull MigrationTarget target);

    int getVersion();

    OptionalInt getOrder();

    @NotNull String getName();

    @NotNull Optional<String> getDescription();

    @NotNull Optional<String> getSourceTableName();

    @NotNull Optional<String> getDestinationTableName();

    @NotNull @UnmodifiableView List<String> getSQLStatements();

    boolean hasSQLStatements();

}
