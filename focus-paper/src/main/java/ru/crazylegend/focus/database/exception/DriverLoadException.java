package ru.crazylegend.focus.database.exception;

import lombok.Getter;
import ru.crazylegend.focus.database.DatabaseType;

@Getter
public class DriverLoadException extends Exception {

    private final DatabaseType databaseType;

    public DriverLoadException(String message, DatabaseType databaseType) {
        this(message, null, databaseType);
    }

    public DriverLoadException(Throwable cause, DatabaseType databaseType) {
        this(cause.getMessage(), cause, databaseType);
    }

    public DriverLoadException(String message, Throwable cause, DatabaseType databaseType) {
        super(message, cause, false, false);
        this.databaseType = databaseType;
    }

}
