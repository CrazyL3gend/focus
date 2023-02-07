package ru.crazylegend.focus.util.math.progress;

import org.bukkit.ChatColor;

public final class ProgressFormats {

    public static final ProgressFormat DEFAULT = ProgressFormat.builder()
            .setSize(10)
            .setYesSymbol(ChatColor.GREEN + "✔")
            .setNoSymbol(ChatColor.RED + "✗")
            .create();

    private ProgressFormats() {
        throw new UnsupportedOperationException();
    }

}
