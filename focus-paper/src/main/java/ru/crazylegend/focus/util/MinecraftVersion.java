package ru.crazylegend.focus.util;

import org.bukkit.Bukkit;

public enum MinecraftVersion {

    v1_8_R1(181),
    v1_8_R2(182),
    v1_8_R3(183),
    v1_9_R1(191),
    v1_9_R2(192),
    v1_10_R1(1101),
    v1_11_R1(1111),
    v1_12_R1(1121),
    v1_13_R1(1131),
    v1_13_R2(1132),
    v1_14_R1(1133),
    v1_15_R1(1135),
    v1_16_R1(1136),
    v1_16_R2(1137),
    v1_16_R3(1138);

    private static final MinecraftVersion current;

    static {
        MinecraftVersion version = v1_13_R1;
        String versionName = null;
        try {
            versionName = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (Exception ignored) {
        }

        if (versionName != null) {
            try {
                version = valueOf(versionName);
            } catch (IllegalArgumentException exception) {
                throw new IllegalStateException("Unsupported server version!");
            }
        }
        current = version;
    }

    private int id;

    MinecraftVersion(int id) {
        this.id = id;
    }

    public static MinecraftVersion getCurrent() {
        return current;
    }

    public int getId() {
        return this.id;
    }

    public boolean isCurrentVersion() {
        return this == current;
    }

    public boolean newerThan(MinecraftVersion other) {
        return id > other.id;
    }

    public boolean newerOrEqual(MinecraftVersion other) {
        return id >= other.id;
    }

    public boolean olderThan(MinecraftVersion other) {
        return id < other.id;
    }

    public boolean olderOrEqual(MinecraftVersion other) {
        return id <= other.id;
    }
}
