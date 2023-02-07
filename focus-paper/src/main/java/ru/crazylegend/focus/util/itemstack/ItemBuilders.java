package ru.crazylegend.focus.util.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class ItemBuilders {

    private ItemBuilders() {
        throw new UnsupportedOperationException();
    }

    public static ItemBuilderImpl from(ItemStack itemStack) {
        return new ItemBuilderImpl(itemStack.clone());
    }

    public static ItemBuilderImpl from(Material type) {
        return from(new ItemStack(type, 1));
    }

    public static ItemBuilderImpl newBuilder() {
        return from(Material.STONE);
    }

    public static SkullItemBuilder skullFrom(ItemStack itemStack) {
        return new SkullItemBuilder(itemStack.clone());
    }

    public static SkullItemBuilder newSkullBuilder() {
        return skullFrom(new ItemStack(Material.SKULL));
    }

    public static LeatherItemBuilder leatherFrom(ItemStack itemStack) {
        return new LeatherItemBuilder(itemStack.clone());
    }

    public static LeatherItemBuilder leatherFrom(Material type) {
        return leatherFrom(new ItemStack(type, 1));
    }

    public static LeatherItemBuilder newLeatherBuilder() {
        return leatherFrom(Material.LEATHER_BOOTS);
    }

    public static BookItemBuilder bookFrom(ItemStack itemStack) {
        return new BookItemBuilder(itemStack.clone());
    }

    public static BookItemBuilder bookFrom(Material type) {
        return bookFrom(new ItemStack(type, 1));
    }

    public static BookItemBuilder newBookBuilder() {
        return bookFrom(Material.WRITTEN_BOOK);
    }

}
