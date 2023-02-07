package ru.crazylegend.focus.database.credential.remote;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;
import ru.crazylegend.focus.database.DatabaseType;
import ru.crazylegend.focus.database.credential.AuthDatabaseCredentials;
import ru.crazylegend.focus.database.credential.CredentialField;
import ru.crazylegend.focus.database.exception.DriverNotFoundException;
import ru.crazylegend.focus.util.tool.Validate;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MySQLDatabaseCredentials implements AuthDatabaseCredentials, RemoteDatabaseCredentials {

    public static final String URL_PATTERN = "jdbc:mysql://%s:%d/%s%s";

    @CredentialField("host")
    private String hostname;
    @CredentialField("port")
    private int port;
    @CredentialField("name")
    private String databaseName;

    @CredentialField("user")
    private String username;
    @CredentialField("password")
    private String password;

    @CredentialField("params")
    private List<String> parameters;

    @Override
    public String getConnectionUrl(Plugin plugin) {
        Validate.notNull(hostname, "hostname");
        Validate.notNull(databaseName, "name");
        Validate.notNull(username, "user");
        Validate.notNull(password, "password");

        return String.format(URL_PATTERN, hostname, port, databaseName, formatParameters());
    }

    private String formatParameters() {
        if (parameters == null || parameters.isEmpty())
            return "";

        String joined = parameters.stream()
                .map(this::urlEncode)
                .collect(Collectors.joining("&"));

        return "?" + joined;
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    private String urlEncode(String source) {
        return URLEncoder.encode(source, "UTF-8");
    }

    @Override
    public void loadDriver(Plugin plugin) throws DriverNotFoundException {
        try {
            Constructor<?> constructor = Class.forName("com.mysql.cj.jdbc.Driver").getConstructor();
            constructor.newInstance();
        } catch (Throwable ignored) {
            try {
                Constructor<?> constructor = Class.forName("com.mysql.jdbc.Driver").getConstructor();
                constructor.newInstance();
            } catch (Throwable ex) {
                throw new DriverNotFoundException(ex, DatabaseType.MYSQL);
            }
        }
    }

}
