package ru.crazylegend.focus.database.quiet;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@FunctionalInterface
public interface ThrowableHandler {

    static @NotNull ThrowableHandler createForDatabase(@NotNull Plugin plugin) {
        return createForDatabase(plugin.getLogger());
    }

    static @NotNull ThrowableHandler createForDatabase(@NotNull Logger logger) {
        return new DatabaseThrowableHandler(logger);
    }

    void handle(@NotNull Throwable throwable);

}
