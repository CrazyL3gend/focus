package ru.crazylegend.focus.util;

import org.bukkit.Bukkit;

public final class PerformCommand {

    private PerformCommand() {
        throw new UnsupportedOperationException();
    }

    public static void perform(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.startsWith("/") ? command.replaceFirst("/", "") : command);
    }

    public static void perform(Iterable<String> commands) {
        commands.forEach(PerformCommand::perform);
    }

}
