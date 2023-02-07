package ru.crazylegend.focus.util.inventory;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventorySlot {

    private int index;
    private ItemStack itemStack;

    public InventorySlot(int index, ItemStack itemStack) {
        this.index = index;
        this.itemStack = itemStack;
    }

    public InventorySlot(int index) {
        this.index = index;
        this.itemStack = null;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void toInventory(Inventory inventory) {
        inventory.setItem(index, itemStack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventorySlot that = (InventorySlot) o;
        return new EqualsBuilder().append(index, that.index).append(itemStack, that.itemStack).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(index).append(itemStack).toHashCode();
    }
}
