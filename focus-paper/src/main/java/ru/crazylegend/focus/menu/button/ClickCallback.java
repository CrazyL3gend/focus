package ru.crazylegend.focus.menu.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface ClickCallback extends InteractableClickCallback {

    @Override
    default boolean process(Player player, ClickType type, int slot) {
        processClick(player, type, slot);
        return true;
    }

    default void processClick(Player player, ClickType type, int slot) {
    }

}
