package ru.crazylegend.focus.util.itemstack;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Map;

public class ImmutableItem extends ItemStack {

    public ImmutableItem(Material type) {
        super(type);
    }

    public ImmutableItem(Material type, int amount) {
        super(type, amount);
    }

    @Deprecated
    public ImmutableItem(Material type, int amount, short damage) {
        super(type, amount, damage);
    }

    public ImmutableItem(ItemStack stack) throws IllegalArgumentException {
        super(stack);
    }

    public static ImmutableItem copyOf(ItemStack itemStack) {
        return new ImmutableItem(itemStack);
    }

    public void setType(Material type) {
        throw new UnsupportedOperationException();
    }

    public void setAmount(int amount) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void setData(MaterialData data) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void setDurability(short durability) {
        throw new UnsupportedOperationException();
    }

    public void addEnchantments(Map<Enchantment, Integer> enchantments) {
        throw new UnsupportedOperationException();
    }

    public void addEnchantment(Enchantment ench, int level) {
        throw new UnsupportedOperationException();
    }

    public void addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        throw new UnsupportedOperationException();
    }

    public void addUnsafeEnchantment(Enchantment ench, int level) {
        throw new UnsupportedOperationException();
    }

    public int removeEnchantment(Enchantment ench) {
        throw new UnsupportedOperationException();
    }

    public boolean setItemMeta(ItemMeta itemMeta) {
        throw new UnsupportedOperationException();
    }

}
