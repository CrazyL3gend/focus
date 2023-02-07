package ru.crazylegend.focus.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ExtendedListener implements Listener {

    protected Plugin plugin;

    protected ExtendedListener(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}
