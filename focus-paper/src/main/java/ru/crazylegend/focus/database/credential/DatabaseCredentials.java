package ru.crazylegend.focus.database.credential;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import ru.crazylegend.focus.database.DatabaseType;
import ru.crazylegend.focus.database.credential.local.LocalDatabaseCredentials;
import ru.crazylegend.focus.database.credential.remote.RemoteDatabaseCredentials;
import ru.crazylegend.focus.database.exception.CredentialsParseException;
import ru.crazylegend.focus.database.exception.DriverLoadException;
import ru.crazylegend.focus.database.exception.DriverNotFoundException;

/**
 * Represents a database credentials store object
 */
public interface DatabaseCredentials {

    /**
     * Try to parse a database credentials from a configuration section
     *
     * @param config       a child (root for credentials) section of config where credentials are located
     * @param databaseType a type of database
     * @return a new parsed instance of {@link DatabaseCredentials}
     * @throws CredentialsParseException when something went wrong
     */
    static DatabaseCredentials parse(ConfigurationSection config, DatabaseType databaseType)
            throws CredentialsParseException {
        return CredentialsParser.parse(config, databaseType);
    }

    /**
     * Get an URL to establish connection to the database
     *
     * @param plugin plugin context
     * @return the connection URL for selected database type
     */
    String getConnectionUrl(Plugin plugin);

    /**
     * Try to load database driver (for PostgreSQL it's an external JDBC driver)
     *
     * @param plugin plugin context
     * @throws DriverNotFoundException when a JDBC driver was not found
     * @throws DriverLoadException     when a JDBC driver cannot be loaded
     */
    void loadDriver(Plugin plugin) throws DriverNotFoundException, DriverLoadException;

    /**
     * Check is auth required for this database type
     *
     * @return 'true' if that required or 'false' overwise
     */
    default boolean isAuthRequired() {
        return false;
    }

    /**
     * Check is this database is based on the local storage file (e.g.: SQLite).
     *
     * @return 'true' if these credentials belongs to a local database or 'false' overwise.
     */
    default boolean isLocalDatabase() {
        return this instanceof LocalDatabaseCredentials;
    }

    /**
     * Check is this database is based on the standalone remote server (e.g.: MySQL).
     *
     * @return 'true' if these credentials belongs to a remote database or 'false' overwise.
     */
    default boolean isRemoteDatabase() {
        return this instanceof RemoteDatabaseCredentials;
    }

}