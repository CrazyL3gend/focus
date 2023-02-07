package ru.crazylegend.focus.database.migration.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.database.migration.Migration;

public class StatementExecuteException extends AbstractMigrationException {

    private static final String MESSAGE_FORMAT = "Couldn't execute SQL statement '%s' from migration '%s': %s";

    public StatementExecuteException(@NotNull Migration migration, @NotNull String statement, @NotNull String message) {
        this(migration, statement, message, null);
    }

    public StatementExecuteException(@NotNull Migration migration, @NotNull String statement, @NotNull Throwable cause) {
        this(migration, statement, cause.getMessage(), cause);
    }

    public StatementExecuteException(@NotNull Migration migration, @NotNull String statement, @NotNull String message, @Nullable Throwable cause) {
        super(String.format(MESSAGE_FORMAT, statement, migration.getId(), message), cause);
    }

}
