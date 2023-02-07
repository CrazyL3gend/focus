package ru.crazylegend.focus.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public final class JsonParser {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("hh:mm:ss dd/MM/yyyy")
            .create();

    private JsonParser() {
        throw new UnsupportedOperationException();
    }

    public static String write(Object object) {
        return GSON.toJson(object);
    }

    public static String write(Object object, Type type) {
        return GSON.toJson(object, type);
    }

    public static <T> T read(String json, Type type) {
        return GSON.fromJson(json, type);
    }

}
