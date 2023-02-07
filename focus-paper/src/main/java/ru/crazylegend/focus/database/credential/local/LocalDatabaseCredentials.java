package ru.crazylegend.focus.database.credential.local;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.database.credential.DatabaseCredentials;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public interface LocalDatabaseCredentials extends DatabaseCredentials {

    /**
     * Get the relative path to storage file.
     *
     * @return The relative file path.
     */
    @NotNull String getFilePath();

    /**
     * Check is the database storage file exists or not.
     *
     * @param plugin The owning plugin of this database.
     * @return A boolean value: the check result.
     */
    default boolean isDatabaseFileExists(@NotNull Plugin plugin) {
        Path storageFilePath = plugin.getDataFolder().toPath().resolve(getFilePath().replace('/', File.separatorChar));
        return Files.isRegularFile(storageFilePath);
    }

}
