package ru.crazylegend.focus.database.migration.config;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import ru.crazylegend.focus.database.migration.Migration;
import ru.crazylegend.focus.database.migration.exception.MigrationParseException;
import ru.crazylegend.focus.database.migration.exception.MigrationResolveException;
import ru.crazylegend.focus.util.tool.Validate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MigrationParser {

    public static final String DEFAULT_MIGRATIONS_PATH_ROOT = "migrations/";
    public static final String MIGRATION_CONFIG_EXTENSION = ".dbsp";

    private static final Pattern HEADER_PATTERN = Pattern.compile("^\\[(.*)]$");
    private static final Pattern KEY_VALUE_PAIR_PATTERN = Pattern.compile("(\\w++)\\s?=\\s?(.++)");

    private final Plugin plugin;
    private final String migrationsPathRoot;
    private final boolean debugLoggingEnabled;

    private final Map<String, Migration> migrations;

    public MigrationParser(@NotNull Plugin plugin) {
        this(plugin, DEFAULT_MIGRATIONS_PATH_ROOT, false);
    }

    public MigrationParser(@NotNull Plugin plugin, boolean debugLoggingEnabled) {
        this(plugin, DEFAULT_MIGRATIONS_PATH_ROOT, debugLoggingEnabled);
    }

    public MigrationParser(@NotNull Plugin plugin, @NotNull String migrationsPathRoot) {
        this(plugin, migrationsPathRoot, false);
    }

    public MigrationParser(@NotNull Plugin plugin, @NotNull String migrationsPathRoot, boolean debugLoggingEnabled) {
        Validate.notNull(plugin, "plugin");
        Validate.notNull(migrationsPathRoot, "migrationsPathRoot");

        this.plugin = plugin;
        this.migrationsPathRoot = migrationsPathRoot;
        this.debugLoggingEnabled = debugLoggingEnabled;
        this.migrations = new LinkedHashMap<>();
    }

    public @NotNull @UnmodifiableView Map<String, Migration> getMigrations() {
        return Collections.unmodifiableMap(migrations);
    }

    public void findMigrationConfigs() throws MigrationResolveException, MigrationParseException {
        String pluginJarPath = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        logDebug("Code source path is '%s'", pluginJarPath);

        File pluginJarFile = new File(pluginJarPath);
        if (!pluginJarFile.isFile())
            throw new IllegalArgumentException(String.format("Code source file '%s' isn't a regular file!", pluginJarPath));

        try {
            JarFile pluginJar = new JarFile(pluginJarFile);
            Enumeration<JarEntry> entries = pluginJar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory())
                    continue;

                String entryName = entry.getName();
                if (entryName.startsWith(migrationsPathRoot) && entryName.endsWith(MIGRATION_CONFIG_EXTENSION)) {
                    logDebug("Found migration config '%s'", entryName);
                    String resourcePath = '/' + entryName;

                    MigrationConfiguration migrationConfig = parseMigrationConfig(resourcePath);
                    Migration migration = migrationConfig.makeInstance();
                    migrations.put(migration.getResourcePath(), migration);

                    logDebug("Successfully loaded migration '%s': %s", migration.getId(), migration);
                }
            }
        } catch (IOException ex) {
            throw new MigrationResolveException(ex);
        }
    }

    public @NotNull MigrationConfiguration parseMigrationConfig(@NotNull String resourcePath) throws MigrationParseException {
        Validate.notEmpty(resourcePath, "resourcePath");

        InputStream resource = plugin.getClass().getResourceAsStream(resourcePath);
        if (resource == null)
            throw new IllegalArgumentException(String.format("Resource '%s' isn't provided by %s!", resourcePath, plugin.getDescription().getName()));

        InputStreamReader inputStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String migrationId = resourcePath.substring(resourcePath.lastIndexOf('/') + 1, resourcePath.length() - 5);
        MigrationConfiguration configuration = new MigrationConfiguration(plugin, resourcePath, migrationId);

        bufferedReader.lines().forEach(line -> {
            if (line.isEmpty() || line.startsWith("#"))
                return;

            if (line.replace(" ", "").isEmpty())
                return;

            Matcher headerMatcher = HEADER_PATTERN.matcher(line);
            if (headerMatcher.find() && headerMatcher.groupCount() == 1) {
                String headerId = headerMatcher.group(1);
                MigrationConfigurationSection section = new MigrationConfigurationSection(headerId);
                configuration.changeCurrentSection(section);
                return;
            }

            configuration.addContentLineForCurrentSection(line);

            Matcher keyValuePairMatcher = KEY_VALUE_PAIR_PATTERN.matcher(line);
            if (keyValuePairMatcher.find() && keyValuePairMatcher.groupCount() == 2) {
                String key = keyValuePairMatcher.group(1);
                String value = keyValuePairMatcher.group(2);
                configuration.addParameterForCurrentSection(key, value);
            }
        });

        return configuration;
    }

    private void logDebug(@NotNull String format, Object... args) {
        if (debugLoggingEnabled)
            log(Logger::info, String.format("[Debug] %s", format), args);
    }

    private void logInfo(@NotNull String format, Object... args) {
        log(Logger::info, format, args);
    }

    private void logWarning(@NotNull String format, Object... args) {
        log(Logger::warning, format, args);
    }

    private void logError(@NotNull String format, Object... args) {
        log(Logger::severe, format, args);
    }

    private void log(@NotNull BiConsumer<Logger, String> loggingFunction, @NotNull String format, Object... args) {
        loggingFunction.accept(plugin.getLogger(), String.format(format, args));
    }

}
