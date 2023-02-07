package ru.crazylegend.focus.database.credential;

public interface AuthDatabaseCredentials extends DatabaseCredentials {

    String getUsername();

    String getPassword();

    @Override
    default boolean isAuthRequired() {
        return true;
    }

}
