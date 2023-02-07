package ru.crazylegend.focus.util.location;

import org.bukkit.Bukkit;
import org.bukkit.World;

public final class WorldUtil {

    private WorldUtil() {
        throw new UnsupportedOperationException();
    }

    public static String checkWorld(String world) {
        if (Bukkit.getWorld(world) == null) {
            throw new NullPointerException("World null!");
        }
        return world;
    }

    public static World getWorld(String world) {
        return Bukkit.getWorld(checkWorld(world));
    }

}
