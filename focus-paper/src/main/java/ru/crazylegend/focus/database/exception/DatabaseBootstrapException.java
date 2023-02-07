package ru.crazylegend.focus.database.exception;


public class DatabaseBootstrapException extends Exception {

    public DatabaseBootstrapException(String message) {
        super(message);
    }

    public DatabaseBootstrapException(Throwable cause) {
        super(cause);
    }

    public DatabaseBootstrapException(String message, Throwable cause) {
        super(message, cause);
    }

}