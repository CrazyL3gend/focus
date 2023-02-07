package ru.crazylegend.focus.menu.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface InteractableClickCallback {

    default boolean process(Player player, ClickType type, int slot) {
        return true;
    }

}