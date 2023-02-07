package ru.crazylegend.focus.util.location;

import org.bukkit.Location;
import org.bukkit.World;
import ru.crazylegend.focus.api.Creatable;

public class LocationBuilder implements Creatable<Location> {

    private World world;
    private double x, y, z;
    private float yaw, pitch;

    private LocationBuilder() {
    }

    public static LocationBuilder newBuilder() {
        return new LocationBuilder();
    }

    public static LocationBuilder from(Location location) {
        return new LocationBuilder()
                .setWorld(location.getWorld())
                .setX(location.getX())
                .setY(location.getY())
                .setZ(location.getZ())
                .setYaw(location.getYaw())
                .setPitch(location.getPitch());
    }

    public static LocationBuilder from(World world) {
        return new LocationBuilder().setWorld(world);
    }

    public LocationBuilder setWorld(World world) {
        this.world = world;
        return this;
    }

    public LocationBuilder setWorld(String world) {
        return setWorld(WorldUtil.getWorld(world));
    }

    public LocationBuilder setX(double x) {
        this.x = x;
        return this;
    }

    public LocationBuilder setY(double y) {
        this.y = y;
        return this;
    }

    public LocationBuilder setZ(double z) {
        this.z = z;
        return this;
    }

    public LocationBuilder setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public LocationBuilder setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    public Location create() {
        return new Location(world, x, y, z, yaw, pitch);
    }
}
