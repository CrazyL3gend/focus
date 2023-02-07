package ru.crazylegend.focus.database.migration.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataConvertationException extends AbstractMigrationException {

    private static final String MESSAGE_FORMAT = "Couldn't convert data from '%s' to '%s': %s";

    public DataConvertationException(@NotNull Class<?> oldDataType, @NotNull Class<?> newDataType, @NotNull String message) {
        this(oldDataType, newDataType, message, null);
    }

    public DataConvertationException(@NotNull Class<?> oldDataType, @NotNull Class<?> newDataType, @NotNull Throwable cause) {
        this(oldDataType, newDataType, cause.getMessage(), cause);
    }

    public DataConvertationException(@NotNull Class<?> oldDataType, @NotNull Class<?> newDataType, @NotNull String message, @Nullable Throwable cause) {
        super(String.format(MESSAGE_FORMAT, oldDataType.getSimpleName(), newDataType.getSimpleName(), message), cause);
    }

}
