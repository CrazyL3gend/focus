package ru.crazylegend.focus.util;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Data
public final class Title {

    private final String title;
    private final String subtitle;
    private final int fadeInTicks;
    private final int stayTicks;
    private final int fadeOutTicks;

    public void send(@NotNull CommandSender receiver) {
        if (receiver instanceof Player) {
            ((Player) receiver).sendTitle(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
            return;
        }

        receiver.sendMessage(ChatColor.RED + "You could see this message if you were player, because it's a title message.");
    }
}