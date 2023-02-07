package ru.crazylegend.focus.database.exception;

import lombok.Getter;
import ru.crazylegend.focus.database.DatabaseType;

@Getter
public class DriverNotFoundException extends Exception {

    private static final String MESSAGE_PATTERN = "Driver for the database type '%s' was not found!";

    private final DatabaseType databaseType;

    public DriverNotFoundException(DatabaseType databaseType) {
        this(String.format(MESSAGE_PATTERN, databaseType.getName()), databaseType);
    }

    public DriverNotFoundException(String message, DatabaseType databaseType) {
        this(message, null, databaseType);
    }

    public DriverNotFoundException(Throwable cause, DatabaseType databaseType) {
        this(String.format(MESSAGE_PATTERN, databaseType.getName()), cause, databaseType);
    }

    public DriverNotFoundException(String message, Throwable cause, DatabaseType databaseType) {
        super(message, cause, false, false);
        this.databaseType = databaseType;
    }

}