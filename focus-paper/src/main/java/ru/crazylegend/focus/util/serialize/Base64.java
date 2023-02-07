package ru.crazylegend.focus.util.serialize;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class Base64 {

    private Base64() {
        throw new UnsupportedOperationException();
    }

    public static <T> String encode(T t) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            try (BukkitObjectOutputStream data = new BukkitObjectOutputStream(output)) {
                data.writeObject(t);
                return java.util.Base64.getEncoder().encodeToString(output.toByteArray());
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T decode(String string) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(string))) {
            try (BukkitObjectInputStream data = new BukkitObjectInputStream(input)) {
                return (T) data.readObject();
            }
        } catch (ClassNotFoundException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
