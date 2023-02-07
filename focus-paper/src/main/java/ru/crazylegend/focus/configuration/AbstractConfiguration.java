package ru.crazylegend.focus.configuration;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.util.tool.Validate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Abstract configuration instance which contains basic methods for working with an YAML configuration.
 */
public abstract class AbstractConfiguration {

    protected final @NotNull JavaPlugin plugin;
    protected final @NotNull Path dataFolderPath;
    protected @NotNull String fileName;
    protected @Nullable InputStream resource;

    protected FileConfiguration configuration;

    /**
     * Create a new abstract configuration instance using default data folder path and configuration resource.
     *
     * @param plugin   The plugin that will use this configuration.
     * @param fileName The configuration file name.
     */
    public AbstractConfiguration(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName
    ) {
        this(plugin, fileName, plugin.getResource(fileName));
    }

    /**
     * Create a new abstract configuration instance using default data folder path.
     *
     * @param plugin   The plugin that will use this configuration.
     * @param fileName The configuration file name.
     * @param resource The configuration resource in the plugin JAR.
     */
    public AbstractConfiguration(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName,
            @Nullable InputStream resource
    ) {
        this(plugin, fileName, plugin.getDataFolder().toPath(), resource);
    }

    /**
     * Create a new abstract configuration instance using all custom values.
     *
     * @param plugin         The plugin that will use this configuration.
     * @param fileName       The configuration file name.
     * @param dataFolderPath The path to data folder.
     * @param resource       The configuration resource in the plugin JAR.
     */
    public AbstractConfiguration(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName,
            @NotNull Path dataFolderPath,
            @Nullable InputStream resource
    ) {
        Validate.notNull(plugin, "plugin");
        Validate.notNull(fileName, "fileName");
        Validate.notNull(dataFolderPath, "dataFolderPath");

        this.plugin = plugin;
        this.fileName = fileName;
        this.dataFolderPath = dataFolderPath;
        this.resource = resource;
    }

    /**
     * @deprecated Use {@link #AbstractConfiguration(JavaPlugin, String, InputStream)} instead.
     */
    @Deprecated
    public AbstractConfiguration(JavaPlugin plugin, InputStream resource, String fileName) {
        this(plugin, fileName, resource);
    }

    /**
     * @deprecated Use {@link #AbstractConfiguration(JavaPlugin, String, Path, InputStream)} instead.
     */
    @Deprecated
    public AbstractConfiguration(JavaPlugin plugin, File dataFolder, InputStream resource, String fileName) {
        this(plugin, fileName, dataFolder.toPath(), resource);
    }

    /**
     * Getting colored string from legacy '&#' colored string
     *
     * @param source - string, colored by '&#'
     * @return new colored string instance
     */
    public static String colorize(String source) {
        return ChatColor.translateAlternateColorCodes('&', source);
    }

    /**
     * Get the Bukkit configuration wrapped by this instance. <br>
     * You can use that to invoke other methods that aren't implemented here.
     *
     * @return A raw Bukkit configuration instance.
     */
    public @NotNull Configuration getBukkitConfig() {
        return configuration;
    }

    /**
     * Get the instance of plugin that uses this configuration.
     *
     * @return The plugin instance.
     */
    public @NotNull JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Get the configuration file name.
     *
     * @return The configuration file name.
     */
    public @NotNull String getFileName() {
        return fileName;
    }

    /**
     * Get the data folder path where an exported configuration file located.
     *
     * @return The path to data folder.
     */
    public @NotNull Path getDataFolderPath() {
        return dataFolderPath;
    }

    /**
     * Get the configuration file resource used to export that to the plugin data folder.
     *
     * @return The configuration file resource inside the plugin JAR.
     */
    public @NotNull Optional<InputStream> getResource() {
        return Optional.ofNullable(resource);
    }

    /**
     * Refresh the configuration file and update the cached configuration structure from that. <br>
     * You can enable debug logging using similar method with a boolean parameter.
     *
     * @see #refresh(boolean)
     */
    public void refresh() {
        refresh(false);
    }

    /**
     * Refresh the configuration file and update the cached configuration structure from that.
     *
     * @param verbose The debug logging boolean flag.
     */
    public void refresh(boolean verbose) {
        Logger logger = plugin.getLogger();

        if (dataFolderPath == null) {
            logger.severe("SKLibrary detected a nulled dataFolderPath field value, check this plugin.");
            return;
        }

        if (!Files.isDirectory(dataFolderPath)) {
            try {
                Files.createDirectories(dataFolderPath);
                sendDebugMessage(verbose, "Created a new data folder in: %s", dataFolderPath.toAbsolutePath());
            } catch (IOException ex) {
                sendErrorMessage(verbose, "Couldn't create a data folder in '%s'!", ex, dataFolderPath.toAbsolutePath());
            }
        }

        Path filePath = dataFolderPath.resolve(getOutputFilePath());
        Path parentDirectory = filePath.getParent();

        if (!Files.isDirectory(parentDirectory)) {
            try {
                Files.createDirectories(parentDirectory);
                sendDebugMessage(verbose, "Created an output file parent directory in: %s", parentDirectory.toAbsolutePath());
            } catch (IOException ex) {
                sendErrorMessage(verbose, "Couldn't create an output file parent directory in '%s'!", ex, parentDirectory.toAbsolutePath());
            }
        }

        if (!Files.isRegularFile(filePath)) {
            try {
                createOutputFile(filePath, verbose);
            } catch (IOException ex) {
                sendErrorMessage(verbose, "Couldn't create a configuration file in '%s'!", ex, filePath.toAbsolutePath());
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(filePath.toFile());
    }

    /********************************
     * 								*
     *    PROXIED BUKKIT METHODS    *
     *								*
     *******************************/

    protected @NotNull String getResourcePath() {
        return fileName;
    }

    protected @NotNull String getOutputFilePath() {
        return fileName.replace('/', File.separatorChar);
    }

    protected void createOutputFile(@NotNull Path filePath, boolean verbose) throws IOException {
        if (resource == null) {
            Files.createFile(filePath);
            sendDebugMessage(verbose, "Created an empty configuration file in: %s", filePath.toAbsolutePath());
        } else {
            Files.copy(resource, filePath, StandardCopyOption.REPLACE_EXISTING);
            sendDebugMessage(verbose, "Copied a configuration file from internal resource to: %s", filePath.toAbsolutePath());
        }
    }

    /**
     * Get the child configuration section by specified path.
     *
     * @param path The configuration section path.
     * @return The found configuration section in this path (nullable).
     */
    public @Nullable ConfigurationSection getSection(@NotNull String path) {
        return configuration.getConfigurationSection(path);
    }

    /**
     * Get the property value as string by specified path.
     *
     * @param path The property path.
     * @return The string value of found configuration property (nullable).
     */
    public @Nullable String getString(@NotNull String path) {
        return configuration.getString(path);
    }

    /**
     * Get the property value as string by specified path or return default value on fail.
     *
     * @param path The property path.
     * @param def  The default value to return on fail.
     * @return The string value of found configuration property or the default value (nullable).
     */
    public @Nullable String getString(@NotNull String path, @Nullable String def) {
        return configuration.getString(path, def);
    }

    /**
     * Getting string from file and replacing '&#' to color codes
     *
     * @param section - section with target string value in file
     * @return received string or null if section not contains string value
     */
    public String getColoredString(String section) {
        String string = configuration.isString(section) ? configuration.getString(section) : null;
        return string == null ? null : colorize(string);
    }

    /**
     * Getting string from file and replacing '&#' to color codes
     *
     * @param section - section with target string value in file
     * @param def     - default output value if section not contains string value
     * @return received string or 'def' value if section not contains string value
     */
    public String getColoredString(String section, String def) {
        String string = configuration.getString(section, def);
        return string == null ? null : colorize(string);
    }

    /**
     * Getting integer from file
     *
     * @param section - section with target integer value in file
     * @return received integer or null if section not contains integer value
     */
    public int getInt(String section) {
        return configuration.getInt(section);
    }

    /**
     * Getting integer from file
     *
     * @param section - section with target integer value in file
     * @param def     - default output value if section not contains integer value
     * @return received integer or 'def' value if section not contains integer value
     */
    public int getInt(String section, int def) {
        return configuration.getInt(section, def);
    }

    /**
     * Getting long from file
     *
     * @param section - section with target long value in file
     * @return received long or null if section not contains long value
     */
    public long getLong(String section) {
        return configuration.getLong(section);
    }

    /**
     * Getting long from file
     *
     * @param section - section with target long value in file
     * @param def     - default output value if section not contains long value
     * @return received long or 'def' value if section not contains long value
     */
    public long getLong(String section, long def) {
        return configuration.getLong(section, def);
    }

    /**
     * Getting double from file
     *
     * @param section - section with target double value in file
     * @return received double or null if section not contains double value
     */
    public double getDouble(String section) {
        return configuration.getDouble(section);
    }

    /**
     * Getting double from file
     *
     * @param section - section with target double value in file
     * @param def     - default output value if section not contains double value
     * @return received double or 'def' value if section not contains double value
     */
    public double getDouble(String section, double def) {
        return configuration.getDouble(section, def);
    }

    /**
     * Getting float from file
     *
     * @param section - section with target float value in file
     * @return received float or null if section not contains float value
     */
    public float getFloat(String section) {
        Object object = configuration.get(section);
        return object instanceof Number ? ((Number) object).floatValue() : 0.0F;
    }

    /**
     * Getting float from file
     *
     * @param section - section with target float value in file
     * @param def     - default output value if section not contains float value
     * @return received float or 'def' value if section not contains float value
     */
    public float getFloat(String section, float def) {
        Object object = configuration.get(section, def);
        return object instanceof Number ? ((Number) object).floatValue() : 0.0F;
    }

    /**
     * Getting boolean from file
     *
     * @param section - section with target boolean value in file
     * @return received boolean or null if section not contains boolean value
     */
    public Boolean getBoolean(String section) {
        return configuration.getBoolean(section);
    }

    /**
     * Getting boolean from file
     *
     * @param section - section with target integer value in file
     * @param def     - default output value if section not contains boolean value
     * @return received boolean or 'def' value if section not contains boolean value
     */
    public boolean getBoolean(String section, boolean def) {
        return configuration.getBoolean(section, def);
    }

    /**
     * Getting strings list from file
     *
     * @param section - section with target strings list in file
     * @return received strings list or null if section not contains strings list
     */
    public List<String> getList(String section) {
        return configuration.getStringList(section);
    }

    /**
     * Getting strings list from file and replacing '&#' to color codes
     *
     * @param section - section with target strings list in file
     * @return received strings list or null if section not contains strings list
     */
    public List<String> getColoredList(String section) {
        List<String> list = configuration.getStringList(section);

        List<String> colored = new ArrayList<>();
        list.forEach(s -> colored.add(colorize(s)));
        return colored;
    }

    /**
     * Formatting your custom message
     *
     * @param message      - the message to format
     * @param replacements - array of string with this syntax: 'placeholder' 'value' 'placeholder' 'value'...
     * @return formatted string with replaced placeholders
     */
    public String format(String message, Object... replacements) {
        if (message == null || replacements == null)
            return message;

        int length = replacements.length;
        if (length == 0)
            return message;

        for (int i = 0; i < length; i += 2) {
            if (i == length - 1) continue;

            String placeholder = replacements[i].toString();
            Object value = replacements[i + 1];

            message = message.replace(placeholder, value != null ? value.toString() : "");
        }

        return message;
    }

    /**
     * Formatting strings list using specified replacements objects array
     *
     * @param list         - target list to format
     * @param replacements - objects array of replacements formatted as '..., key, value, ...'
     * @return formatted strings list with replaced placeholders
     */
    public List<String> formatList(List<String> list, Object... replacements) {
        if (list == null || replacements == null || list.isEmpty())
            return list;

        return list.stream()
                .map(s -> format(s, replacements))
                .collect(Collectors.toList());
    }

    // --- logging
    protected void sendDebugMessage(boolean debugEnabled, @NotNull String message, Object... args) {
        if (debugEnabled)
            plugin.getLogger().info(String.format(message, args));
    }

    /********************************
     * 								*
     *    BACKWARD COMPATIBILITY    *
     * 								*
     *******************************/

    protected void sendErrorMessage(@NotNull String message, Object... args) {
        plugin.getLogger().severe(String.format(message, args));
    }

    protected void sendErrorMessage(boolean debugEnabled, @NotNull String message, @NotNull Throwable exception, Object... args) {
        sendErrorMessage(message, args);
        sendErrorMessage("Reason: %s", exception.getMessage());
        if (debugEnabled)
            exception.printStackTrace();
    }

    /**
     * @deprecated Use {@link #getBukkitConfig()} instead.
     */
    @Deprecated
    public @NotNull FileConfiguration getFileConfig() {
        return configuration;
    }

    /**
     * @deprecated Use {@link #getFileName()} instead.
     */
    @Deprecated
    public @NotNull String getFilename() {
        return fileName;
    }

    /**
     * @deprecated Use {@link #getDataFolderPath()} instead.
     */
    @Deprecated
    public @NotNull File getDatafolder() {
        return dataFolderPath.toFile();
    }

    /**
     * @deprecated Use {@link #getResource()} instead.
     */
    @Deprecated
    public @Nullable InputStream getSource() {
        return resource;
    }

}
