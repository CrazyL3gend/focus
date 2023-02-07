package ru.crazylegend.focus.configuration;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Configuration for working with configuration files
 */
public class Configuration extends AbstractConfiguration {

    /**
     * Create a new messages instance using default data folder path and configuration resource.
     *
     * @param plugin   The plugin that will use this configuration.
     * @param fileName The configuration file name.
     */
    public Configuration(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName
    ) {
        super(plugin, fileName);
    }

    /**
     * Create a new messages instance using default data folder path.
     *
     * @param plugin   The plugin that will use this configuration.
     * @param fileName The configuration file name.
     * @param resource The configuration resource in the plugin JAR.
     */
    public Configuration(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName,
            @NotNull InputStream resource
    ) {
        super(plugin, fileName, resource);
    }

    /**
     * Create a new messages instance using all custom values.
     *
     * @param plugin         The plugin that will use this configuration.
     * @param fileName       The configuration file name.
     * @param dataFolderPath The path to data folder.
     * @param resource       The configuration resource in the plugin JAR.
     */
    public Configuration(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName,
            @NotNull Path dataFolderPath,
            @NotNull InputStream resource
    ) {
        super(plugin, fileName, dataFolderPath, resource);
    }

    /**
     * @deprecated Use {@link #Configuration(JavaPlugin, String, InputStream)} instead.
     */
    @Deprecated
    public Configuration(JavaPlugin plugin, InputStream resource, String fileName) {
        this(plugin, fileName, resource);
    }

    /**
     * @deprecated Use {@link #Configuration(JavaPlugin, String, Path, InputStream)} instead.
     */
    @Deprecated
    public Configuration(JavaPlugin plugin, File dataFolder, InputStream resource, String fileName) {
        this(plugin, fileName, dataFolder.toPath(), resource);
    }

}
