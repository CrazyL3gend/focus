package ru.crazylegend.focus.util.math;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CuboidRegion {

    private Location min, max;
    private int width, height, depth;
    private Set<Location> extremePoints;

    public CuboidRegion(Location min, Location max) {
        if (min.getWorld() == null || !min.getWorld().equals(max.getWorld())) {
            throw new IllegalArgumentException("Locations in two different worlds!");
        }
        this.min = min;
        this.max = max;

        width = max.getBlockX() - min.getBlockX();
        height = max.getBlockY() - min.getBlockY();
        depth = max.getBlockZ() - min.getBlockZ();

        World world = min.getWorld();
        int minX = min.getBlockX(), minY = min.getBlockY(), minZ = min.getBlockZ();
        int maxX = max.getBlockX(), maxY = max.getBlockY(), maxZ = max.getBlockZ();

        List<Location> extremePoints = new ArrayList<>();
        extremePoints.add(min);
        extremePoints.add(max);
        extremePoints.add(new Location(world, maxX, maxY, minZ));
        extremePoints.add(new Location(world, maxX, minY, maxZ));
        extremePoints.add(new Location(world, minX, maxY, maxZ));
        extremePoints.add(new Location(world, minX, minY, maxZ));
        extremePoints.add(new Location(world, minX, maxY, minZ));
        extremePoints.add(new Location(world, minX, minY, maxZ));

        this.extremePoints = ImmutableSet.copyOf(extremePoints);
    }

    public CuboidRegion(Block min, Block max) {
        this(min.getLocation(), max.getLocation());
    }

    public boolean contains(Location location) {
        if (location.getWorld() == null) {
            return false;
        }
        return location.getWorld().equals(min.getWorld()) &&
                min.getBlockX() <= location.getBlockX() && max.getBlockX() >= location.getBlockX() &&
                min.getBlockY() <= location.getBlockY() && max.getBlockY() >= location.getBlockY() &&
                min.getBlockZ() <= location.getBlockZ() && max.getBlockZ() >= location.getBlockZ();
    }

    public boolean isCollapse(CuboidRegion region) {
        Location minThis = min;
        Location maxThis = max;

        Location minThat = region.min;
        Location maxThat = region.max;

        return (maxThis.getX() >= minThat.getX()) &&
                (minThis.getX() <= maxThat.getX()) &&
                (maxThis.getY() >= minThat.getY()) &&
                (minThis.getY() <= maxThat.getY()) &&
                (maxThis.getZ() >= minThat.getZ()) &&
                (minThis.getZ() <= maxThat.getZ());
    }

    public Set<Block> getBlocks() {
        Set<Block> blocks = new HashSet<>();
        World world = min.getWorld();
        if (world == null) {
            return ImmutableSet.of();
        }
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return ImmutableSet.copyOf(blocks);
    }

    public void forEach(Consumer<Block> action) {
        getBlocks().forEach(action);
    }

    public double getVolume() {
        return width * height * depth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public World getWorld() {
        return min.getWorld();
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }

    public Set<Location> getExtremePoints() {
        return extremePoints;
    }
}
