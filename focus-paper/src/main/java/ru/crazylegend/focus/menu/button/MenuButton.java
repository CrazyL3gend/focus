package ru.crazylegend.focus.menu.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuButton implements InteractableClickCallback {

    private ItemStack itemStack;
    private List<InteractableClickCallback> callbacks;

    public MenuButton(ItemStack itemStack, InteractableClickCallback callback) {
        this.itemStack = itemStack;
        this.callbacks = new ArrayList<>();
        callbacks.add(callback);
    }

    public MenuButton(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.callbacks = new ArrayList<>();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public boolean process(Player player, ClickType type, int slot) {
        boolean result = true;
        for (InteractableClickCallback callback : callbacks) {
            if (!callback.process(player, type, slot)) {
                result = false;
            }
        }
        return result;
    }

}
