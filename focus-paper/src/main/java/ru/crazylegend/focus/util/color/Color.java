package ru.crazylegend.focus.util.color;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import ru.crazylegend.focus.util.Enums;

public enum Color {

    WHITE(DyeColor.WHITE, ChatColor.WHITE, 0),
    ORANGE(DyeColor.ORANGE, ChatColor.GOLD, 1),
    PINK(DyeColor.PINK, ChatColor.LIGHT_PURPLE, 2),
    AQUA(DyeColor.LIGHT_BLUE, ChatColor.AQUA, 3),
    YELLOW(DyeColor.YELLOW, ChatColor.YELLOW, 4),
    LIME(DyeColor.LIME, ChatColor.GREEN, 5),
    DARK_GREEN(DyeColor.MAGENTA, ChatColor.DARK_GREEN, 6),
    DARK_GRAY(DyeColor.GRAY, ChatColor.DARK_GRAY, 7),
    GRAY(DyeColor.SILVER, ChatColor.GRAY, 8),
    CYAN(DyeColor.CYAN, ChatColor.DARK_AQUA, 9),
    PURPLE(DyeColor.PURPLE, ChatColor.DARK_PURPLE, 10),
    BLUE(DyeColor.BLACK, ChatColor.BLUE, 11),
    BROWN(DyeColor.BROWN, ChatColor.DARK_RED, 12),
    GREEN(DyeColor.GREEN, ChatColor.GREEN, 13),
    RED(DyeColor.RED, ChatColor.RED, 14),
    BLACK(DyeColor.BLACK, ChatColor.BLACK, 15);

    private DyeColor dye;
    private ChatColor chat;
    private byte data;

    Color(DyeColor dye, ChatColor chat, int data) {
        this.dye = dye;
        this.chat = chat;
        this.data = (byte) data;
    }

    public static Color fromString(String string) {
        return Enums.valueOf(values(), string.toUpperCase());
    }

    public DyeColor getDye() {
        return dye;
    }

    public org.bukkit.Color getColor() {
        return dye.getColor();
    }

    public ChatColor getChat() {
        return chat;
    }

    public byte getData() {
        return data;
    }

}

