package ru.crazylegend.focus.task.async;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.task.AbstractPluginTask;

public abstract class AsyncPluginTask extends AbstractPluginTask {

    protected AsyncPluginTask(@NotNull Plugin plugin) {
        super(plugin);
    }

    protected AsyncPluginTask(@NotNull Plugin plugin, long delay) {
        super(plugin, delay);
    }

    @Override
    public void start() {
        this.bukkitTask = runTaskTimer(true);
    }

}
