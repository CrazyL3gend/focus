package ru.crazylegend.focus.database.migration.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MigrationParseException extends AbstractMigrationException {

    private static final String MESSAGE_FORMAT = "Couldn't correctly parse a migration config from '%s': %s";

    public MigrationParseException(@NotNull String path, @NotNull String message) {
        this(path, message, null);
    }

    public MigrationParseException(@NotNull String path, @NotNull Throwable cause) {
        this(path, cause.getMessage(), cause);
    }

    public MigrationParseException(@NotNull String path, @NotNull String message, @Nullable Throwable cause) {
        super(String.format(MESSAGE_FORMAT, path, message), cause);
    }

}
