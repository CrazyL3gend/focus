package ru.crazylegend.focus.util.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.crazylegend.focus.util.serialize.Base64;

public final class LocationSerializers {

    private LocationSerializers() {
        throw new UnsupportedOperationException();
    }

    public static LocationSerializer string() {
        return StringLocationSerializer.INST;
    }

    public static LocationSerializer base64() {
        return Base64LocationSerializer.INST;
    }

    public static YamlLocationSerializer yaml() {
        return YamlLocationSerializerImpl.INST;
    }

    private enum StringLocationSerializer implements LocationSerializer {
        INST;

        @Override
        public String serialize(Location location) {
            if (location == null) {
                return "";
            }
            World world = location.getWorld();
            if (world == null) {
                return "";
            }
            float yaw = location.getYaw(), pitch = location.getPitch();
            String string = world.getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ();
            if ((yaw + pitch) != 0) {
                string += " " + yaw + " " + pitch;
            }
            return string;
        }

        @Override
        public Location deserialize(String string) {
            if (string == null) {
                return null;
            }
            String[] split = string.split(" ");
            World world = WorldUtil.getWorld(split[0]);

            Location location = new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));

            if (split.length == 6) {
                location.setYaw(Float.parseFloat(split[4]));
                location.setPitch(Float.parseFloat(split[5]));
            }

            return location;
        }
    }

    private enum Base64LocationSerializer implements LocationSerializer {
        INST;

        @Override
        public String serialize(Location location) {
            if (location == null) {
                return "";
            }
            Position position = Position.from(location);
            if (position == null) {
                return "";
            }
            return Base64.encode(position);
        }

        @Override
        public Location deserialize(String string) {
            if (string == null) {
                return null;
            }
            Position position = Base64.decode(string);

            return position.toLocation();
        }

        private static class Position {

            private String world;
            private double x, y, z;
            private float yaw, pitch;

            private Position(String world, double x, double y, double z, float yaw, float pitch) {
                this.world = world;
                this.x = x;
                this.y = y;
                this.z = z;
                this.yaw = yaw;
                this.pitch = pitch;
            }

            public static Position from(Location location) {
                World world = location.getWorld();
                if (world == null) {
                    return null;
                }
                return new Position(world.getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            }

            public Location toLocation() {
                Location location = new Location(WorldUtil.getWorld(world), x, y, z);
                if ((yaw + pitch) != 0) {
                    location.setYaw(yaw);
                    location.setPitch(pitch);
                }
                return location;
            }
        }
    }

    private enum YamlLocationSerializerImpl implements YamlLocationSerializer {

        INST;

        @Override
        public ConfigurationSection serialize(YamlConfiguration config, String path, Location location) {
            return null;
        }

        @Override
        public Location deserialize(Configuration config, String path) {
            return null;
        }
    }

}
