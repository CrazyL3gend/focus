package ru.crazylegend.focus.database;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.crazylegend.focus.database.credential.DatabaseCredentials;
import ru.crazylegend.focus.database.credential.local.SQLiteDatabaseCredentials;
import ru.crazylegend.focus.database.credential.remote.MySQLDatabaseCredentials;
import ru.crazylegend.focus.database.credential.remote.PostgreSQLDatabaseCredentials;
import ru.crazylegend.focus.database.exception.UnknownDatabaseTypeException;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DatabaseType {

    SQLITE("sqlite", "SQLite", SQLiteDatabaseCredentials.class),
    MYSQL("mysql", "MySQL", MySQLDatabaseCredentials.class),
    POSTGRESQL("postgresql", "PostgreSQL", PostgreSQLDatabaseCredentials.class);

    private final String id;
    private final String name;
    private final Class<? extends DatabaseCredentials> providingClass;

    public static DatabaseType getById(String id) throws UnknownDatabaseTypeException {
        if (id == null)
            throw new UnknownDatabaseTypeException("null");

        DatabaseType databaseType = Arrays.stream(values())
                .filter(type -> type.id.equalsIgnoreCase(id))
                .findAny().orElse(null);

        if (databaseType == null)
            throw new UnknownDatabaseTypeException(id);

        return databaseType;
    }

    @Override
    public String toString() {
        return id;
    }

}
