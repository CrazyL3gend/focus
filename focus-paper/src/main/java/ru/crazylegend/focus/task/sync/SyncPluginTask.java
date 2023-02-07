package ru.crazylegend.focus.task.sync;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.task.AbstractPluginTask;

public abstract class SyncPluginTask extends AbstractPluginTask {

    protected SyncPluginTask(@NotNull Plugin plugin) {
        super(plugin);
    }

    protected SyncPluginTask(@NotNull Plugin plugin, long delay) {
        super(plugin, delay);
    }

    @Override
    public void start() {
        this.bukkitTask = runTaskTimer(false);
    }

}
