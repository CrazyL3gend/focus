package ru.crazylegend.focus.util.itemstack;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherItemBuilder extends AbstractItemBuilder<LeatherItemBuilder, LeatherArmorMeta> {

    LeatherItemBuilder(ItemStack pattern) {
        super(pattern);

        Material type = pattern.getType();
        if (type != Material.LEATHER_BOOTS && type != Material.LEATHER_CHESTPLATE && type != Material.LEATHER_HELMET && type != Material.LEATHER_LEGGINGS) {
            throw new IllegalArgumentException("Pattern type is not supported!");
        }
    }

    public LeatherItemBuilder setColor(Color color) {
        meta.setColor(color);
        return this;
    }

    public LeatherItemBuilder setColor(ru.crazylegend.focus.util.color.Color color) {
        return setColor(color.getColor());
    }

}
