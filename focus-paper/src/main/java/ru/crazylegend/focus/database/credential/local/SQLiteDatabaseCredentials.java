package ru.crazylegend.focus.database.credential.local;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import ru.crazylegend.focus.database.DatabaseType;
import ru.crazylegend.focus.database.credential.CredentialField;
import ru.crazylegend.focus.database.credential.DatabaseCredentials;
import ru.crazylegend.focus.database.exception.DriverNotFoundException;
import ru.crazylegend.focus.util.tool.Validate;

import java.io.File;
import java.lang.reflect.Constructor;

@Getter
public class SQLiteDatabaseCredentials implements DatabaseCredentials, LocalDatabaseCredentials {

    public static final String URL_PATTERN = "jdbc:sqlite:%s";

    @CredentialField("file")
    private String filePath;

    @Override
    public String getConnectionUrl(Plugin plugin) {
        Validate.notNull(filePath, "filePath");

        String path = new File(plugin.getDataFolder(), filePath.replace('/', File.separatorChar)).getAbsolutePath();
        return String.format(URL_PATTERN, path);
    }

    @Override
    public void loadDriver(Plugin plugin) throws DriverNotFoundException {
        try {
            Constructor<?> constructor = Class.forName("org.sqlite.JDBC").getConstructor();
            constructor.newInstance();
        } catch (Throwable ex) {
            throw new DriverNotFoundException(ex, DatabaseType.SQLITE);
        }
    }

}
