package ru.crazylegend.focus.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PluginTask extends Runnable {

    @NotNull Plugin getPlugin();

    @Nullable BukkitTask getRunningTask();

    boolean isRunning();

    void start();

    void restart();

    void shutdown();

}
