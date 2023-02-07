package ru.crazylegend.focus.util.component.replacement;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Replacements {

    public static TextReplacement text(String placeholder, Object value) {
        return new TextReplacement(placeholder, String.valueOf(value));
    }

    public static TextReplacement[] text(Object... replacementsRaw) {
        if (replacementsRaw == null) return null;

        int length = replacementsRaw.length;
        if (replacementsRaw.length == 0) return null;

        List<TextReplacement> replacements = new ArrayList<>();
        for (int i = 0; i < length; i += 2) {
            if (i == length - 1) continue;

            String placeholder = replacementsRaw[i].toString();
            String value = replacementsRaw[i + 1].toString();

            replacements.add(text(placeholder, value));
        }

        return replacements.toArray(new TextReplacement[0]);
    }

    public static TextComponentReplacement component(String placeholder, Object value) {
        return component(placeholder, new TextComponent(colorize(String.valueOf(value))));
    }

    public static TextComponentReplacement component(String placeholder, TextComponent value) {
        return new TextComponentReplacement(placeholder, value);
    }

    public static TextComponentReplacement[] components(Object... replacementsRaw) {
        if (replacementsRaw == null) return null;

        int length = replacementsRaw.length;
        if (replacementsRaw.length == 0) return null;

        List<TextComponentReplacement> replacements = new ArrayList<>();
        for (int i = 0; i < length; i += 2) {
            if (i == length - 1) continue;

            String placeholder = replacementsRaw[i].toString();
            Object value = replacementsRaw[i + 1];

            if (value instanceof TextComponent)
                replacements.add(component(placeholder, (TextComponent) value));
        }

        return replacements.toArray(new TextComponentReplacement[0]);
    }

    private static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
