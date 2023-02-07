package ru.crazylegend.focus.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import ru.crazylegend.focus.util.function.Predicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class BlockRadius {

    private BlockRadius() {
        throw new UnsupportedOperationException();
    }

    public static List<Block> selectBlocks(Location location, int radius, Predicate<Block> predicate) {
        List<Block> blocks = new ArrayList<>();
        World world = location.getWorld();
        if (world == null) {
            return blocks;
        }
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (predicate.test(block)) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static List<Block> selectBlocks(Location location, int radius) {
        return selectBlocks(location, radius, Predicates.alwaysTrue());
    }

    public static void acceptBlocks(Location location, int radius, Predicate<Block> predicate, Consumer<Block> consumer) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (predicate.test(block)) {
                        consumer.accept(block);
                    }
                }
            }
        }
    }

    public static void acceptBlocks(Location location, int radius, Consumer<Block> consumer) {
        acceptBlocks(location, radius, Predicates.alwaysTrue(), consumer);
    }

}
