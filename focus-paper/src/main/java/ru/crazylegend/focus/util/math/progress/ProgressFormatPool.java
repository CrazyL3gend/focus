package ru.crazylegend.focus.util.math.progress;

import java.util.HashMap;
import java.util.Map;

public class ProgressFormatPool {

    private static final Map<String, ProgressFormat> map = new HashMap<>();

    private ProgressFormatPool() {
        throw new UnsupportedOperationException();
    }

    public static ProgressFormat register(String key, ProgressFormat format) {
        map.put(key, format);
        return format;
    }

    public static ProgressFormat get(String key) {
        return map.get(key);
    }

}
