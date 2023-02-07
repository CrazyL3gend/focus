package ru.crazylegend.focus.database.exception;

import lombok.Getter;
import ru.crazylegend.focus.database.DatabaseType;

import java.util.Arrays;

@Getter
public class UnknownDatabaseTypeException extends Exception {

    private static final String MESSAGE_PATTERN = "Unknown database type '%s', supported: %s!";
    private static final String SUPPORTED_TYPES = Arrays.toString(DatabaseType.values());

    private final String id;

    public UnknownDatabaseTypeException(String id) {
        super(String.format(MESSAGE_PATTERN, id, SUPPORTED_TYPES));
        this.id = id;
    }

}