package ru.crazylegend.focus.task.async;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.configuration.Configuration;
import ru.crazylegend.focus.task.AbstractConfiguredTask;


public abstract class AsyncConfiguredTask extends AbstractConfiguredTask {

    protected AsyncConfiguredTask(@NotNull Plugin plugin, @NotNull Configuration config, @NotNull String configurationRootPath) {
        super(plugin, config, configurationRootPath);
    }

    @Override
    public void start() {
        if (config.getBoolean(configurationRootPath + ".enabled", true))
            this.bukkitTask = runTaskTimer(true);
    }

}
