package ru.crazylegend.focus.database.migration.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.database.migration.Migration;

public class MigrationRunException extends AbstractMigrationException {

    private static final String MESSAGE_FORMAT = "Couldn't run migration '%s': %s";

    public MigrationRunException(@NotNull Migration migration, @NotNull String message) {
        this(migration, message, null);
    }

    public MigrationRunException(@NotNull Migration migration, @NotNull Throwable cause) {
        this(migration, cause.getMessage(), cause);
    }

    public MigrationRunException(@NotNull Migration migration, @NotNull String message, @Nullable Throwable cause) {
        super(String.format(MESSAGE_FORMAT, migration.getName(), message), cause);
    }

}
