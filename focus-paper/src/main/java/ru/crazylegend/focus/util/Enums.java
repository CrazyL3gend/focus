package ru.crazylegend.focus.util;

public final class Enums {

    private Enums() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Enum<T>> T valueOf(T[] values, String string) {
        for (T value : values) {
            if (value.name().equalsIgnoreCase(string)) {
                return value;
            }
        }
        return null;
    }

}
