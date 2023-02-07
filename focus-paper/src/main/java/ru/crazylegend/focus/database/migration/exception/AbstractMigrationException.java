package ru.crazylegend.focus.database.migration.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMigrationException extends Exception {

    public AbstractMigrationException(@NotNull String message) {
        this(message, null);
    }

    public AbstractMigrationException(@NotNull Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public AbstractMigrationException(@NotNull String message, @Nullable Throwable cause) {
        super(message, cause);
    }

}
