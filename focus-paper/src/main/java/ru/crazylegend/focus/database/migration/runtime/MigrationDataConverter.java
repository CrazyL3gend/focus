package ru.crazylegend.focus.database.migration.runtime;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MigrationDataConverter<OLD, NEW> {

    @NotNull NEW convert(@NotNull OLD old) throws Throwable;

}
