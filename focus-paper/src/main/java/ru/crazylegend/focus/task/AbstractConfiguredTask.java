package ru.crazylegend.focus.task;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.configuration.Configuration;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.concurrent.TimeUnit;

public abstract class AbstractConfiguredTask extends AbstractPluginTask implements ConfiguredTask {

    protected final Configuration config;
    protected final String configurationRootPath;

    protected AbstractConfiguredTask(@NotNull Plugin plugin, @NotNull Configuration config, @NotNull String configurationRootPath) {
        super(plugin);

        Validate.notNull(config, "config");
        Validate.notNull(configurationRootPath, "configurationRootPath");

        this.config = config;
        this.configurationRootPath = configurationRootPath;
    }

    @Override
    public @NotNull Configuration getConfig() {
        return config;
    }

    @Override
    public @NotNull String getConfigurationRootPath() {
        return configurationRootPath;
    }

    protected long getPeriod(@NotNull TimeUnit timeUnit) {
        return getPeriod(timeUnit, -1);
    }

    protected long getPeriod(@NotNull TimeUnit timeUnit, int minimalValue) {
        int value = config.getInt(configurationRootPath + ".period", minimalValue);
        return timeUnit.toSeconds(Math.max(value, minimalValue)) * 20L;
    }

}
