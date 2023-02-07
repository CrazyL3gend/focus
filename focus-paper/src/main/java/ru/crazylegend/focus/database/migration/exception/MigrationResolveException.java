package ru.crazylegend.focus.database.migration.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MigrationResolveException extends AbstractMigrationException {

    public MigrationResolveException(@NotNull String message) {
        this(message, null);
    }

    public MigrationResolveException(@NotNull Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public MigrationResolveException(@NotNull String message, @Nullable Throwable cause) {
        super(message, cause);
    }

}
