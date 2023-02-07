package ru.crazylegend.focus.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.util.tool.Validate;

public abstract class AbstractPluginTask implements PluginTask {

    protected final Plugin plugin;
    protected final long delay;
    protected BukkitTask bukkitTask;

    protected AbstractPluginTask(@NotNull Plugin plugin) {
        this(plugin, 0L);
    }

    protected AbstractPluginTask(@NotNull Plugin plugin, long delay) {
        Validate.notNull(plugin, "plugin");
        Validate.isTrue(delay >= 0L, "'delay' cannot be less than 0!");

        this.plugin = plugin;
        this.delay = delay;
    }

    protected abstract long getPeriod();

    protected @Nullable BukkitTask runTaskTimer(boolean async) {
        long period = getPeriod();
        if (period < 0L)
            period = 0L;

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        if (async)
            return scheduler.runTaskTimerAsynchronously(plugin, this, delay, period);
        else
            return scheduler.runTaskTimer(plugin, this, delay, period);
    }

    @Override
    public void run() {
        start();
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    @Override
    public @Nullable BukkitTask getRunningTask() {
        return bukkitTask;
    }

    @Override
    public boolean isRunning() {
        return bukkitTask != null && !bukkitTask.isCancelled();
    }

    @Override
    public void restart() {
        shutdown();
        start();
    }

    @Override
    public void shutdown() {
        if (isRunning())
            bukkitTask.cancel();
    }

}
