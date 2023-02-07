package ru.crazylegend.focus.util.itemstack;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullItemBuilder extends AbstractItemBuilder<SkullItemBuilder, SkullMeta> {

    SkullItemBuilder(ItemStack pattern) {
        super(pattern);

        Material type = pattern.getType();
        if (type != Material.SKULL) {
            throw new IllegalArgumentException("Pattern type is not supported!");
        }
    }

    @Deprecated
    public SkullItemBuilder setOwner(String owner) {
        meta.setOwner(owner);
        return this;
    }

    public SkullItemBuilder setOwner(OfflinePlayer player) {
        meta.setOwningPlayer(player);
        return this;
    }

}
