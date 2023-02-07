package ru.crazylegend.focus.util.tool;

import java.util.Collection;

public class Validate {

    public static void notNull(Object object, String fieldName) {
        if (object == null)
            throw new IllegalArgumentException("'" + fieldName + "' cannot be null!");
    }

    public static void notEmpty(String string, String fieldName) {
        if (string == null || string.isEmpty())
            throw new IllegalArgumentException("'" + fieldName + "' cannot be empty!");
    }

    public static void notEmpty(Object[] array, String fieldName) {
        if (array == null || array.length == 0)
            throw new IllegalArgumentException("'" + fieldName + "' cannot be empty!");
    }

    public static void notEmpty(Collection<?> collection, String fieldName) {
        if (collection == null || collection.isEmpty())
            throw new IllegalArgumentException("'" + fieldName + "' cannot be empty!");
    }

    public static void isTrue(boolean condition, String message) {
        if (!condition)
            throw new IllegalArgumentException(message);
    }

    public static void isFalse(boolean condition, String message) {
        if (condition)
            throw new IllegalArgumentException(message);
    }

}