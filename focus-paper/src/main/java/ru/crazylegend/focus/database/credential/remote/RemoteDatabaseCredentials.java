package ru.crazylegend.focus.database.credential.remote;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.database.credential.DatabaseCredentials;

import java.sql.SQLException;

public interface RemoteDatabaseCredentials extends DatabaseCredentials {

    String CREATE_DATABASE_STATEMENT = "CREATE DATABASE `%s`;";

    /**
     * Get the remote database name.
     *
     * @return The database name.
     */
    @NotNull String getDatabaseName();

    /**
     * Try to create the database with specified name.
     *
     * @param connectionSource The connection source that will be used to check.
     * @return A boolean value: 'true' if the remote database has been created, overwise 'false'.
     * @throws SQLException If something went wrong.
     */
    default boolean createDatabaseIfNotExists(@NotNull ConnectionSource connectionSource) throws SQLException {
        DatabaseConnection connection = connectionSource.getReadWriteConnection(null);
        int updateCount = connection.executeStatement(String.format(CREATE_DATABASE_STATEMENT, getDatabaseName()), DatabaseConnection.DEFAULT_RESULT_FLAGS);
        return updateCount != 0;
    }

}
