package ru.crazylegend.focus.database.credential;

import org.bukkit.configuration.ConfigurationSection;
import ru.crazylegend.focus.database.DatabaseType;
import ru.crazylegend.focus.database.exception.CredentialsParseException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

final class CredentialsParser {

    @SuppressWarnings("unchecked")
    static <T extends DatabaseCredentials> T parse(ConfigurationSection config, DatabaseType databaseType)
            throws CredentialsParseException {
        Class<T> providingClass = (Class<T>) databaseType.getProvidingClass();
        T credentials;

        // create new instance
        try {
            Constructor<T> constructor = providingClass.getConstructor();
            credentials = constructor.newInstance();
        } catch (NoSuchMethodException ex) {
            throw new CredentialsParseException("A non-args constructor was not found in " + providingClass.getName() + "!", databaseType);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException ex) {
            throw new CredentialsParseException("Cannot invoke a non-args constructor in " + providingClass.getName() + "!", ex, databaseType);
        }

        // get configuration keys
        Map<String, Object> keys = config.getValues(false);

        // working with fields
        Field[] fields = providingClass.getDeclaredFields();

        for (Field field : fields) {
            CredentialField annotation = field.getAnnotation(CredentialField.class);
            if (annotation == null)
                continue;

            String fieldName = field.getName();
            String configField = annotation.value();
            if (configField == null)
                continue;

            Object configValue = keys.get(configField);
            if (configValue == null)
                continue;

            try {
                field.setAccessible(true);
            } catch (SecurityException ex) {
                throw new CredentialsParseException("Couldn't make field '" + fieldName + "' accessible!", databaseType);
            }

            try {
                field.set(credentials, configValue);
            } catch (IllegalAccessException ex) {
                throw new CredentialsParseException("Couldn't modify value of field '" + fieldName + "'!", databaseType);
            }
        }

        return credentials;
    }

}
