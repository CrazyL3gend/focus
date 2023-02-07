package ru.crazylegend.focus.util.player;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public final class PlayerUtils {

    private PlayerUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean giveItems(Player player, Collection<ItemStack> items) {
        boolean result = false;
        Location location = player.getLocation();
        World world = location.getWorld();
        Inventory inventory = player.getInventory();
        for (ItemStack itemStack : items) {
            if (inventory.firstEmpty() == -1) {
                if (world == null) {
                    throw new NullPointerException();
                }
                world.dropItemNaturally(location, itemStack);
                result = true;
                continue;
            }
            inventory.addItem(itemStack);
        }
        return result;
    }

    public static boolean giveItems(Player player, ItemStack... items) {
        return giveItems(player, Lists.newArrayList(items));
    }

    public static double getDistance(Player player, Location location) {
        Location playerLocation = player.getLocation();
        World world = playerLocation.getWorld();
        if (world == null || !world.equals(location.getWorld())) {
            return -1;
        }
        return playerLocation.distance(location);
    }

}
