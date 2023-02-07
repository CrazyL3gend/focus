package ru.crazylegend.focus.util.actionbar;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Actionbar {

    String text;

    public Actionbar(String text) {
        this.text = text;
    }

    Actionbar(Actionbar actionbar) {
        this(actionbar.getText());
    }

    public String getText() {
        return text;
    }

    public void send(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
    }

    public void broadcast() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    public AdaptiveActionbar toAdaptive() {
        return AdaptiveActionbar.adapt(this);
    }
}
