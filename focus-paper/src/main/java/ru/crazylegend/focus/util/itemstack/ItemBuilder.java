package ru.crazylegend.focus.util.itemstack;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.crazylegend.focus.api.Creatable;

import java.util.Collection;
import java.util.Map;

public interface ItemBuilder<B extends ItemBuilder<B>> extends Creatable<ItemStack> {

    B setType(Material type);

    B setAmount(int amount);

    @Deprecated
    B setDurability(short durability);

    B addEnchantments(Map<Enchantment, Integer> enchantments);

    B addEnchantment(Enchantment enchantment, int level);

    B setDisplay(String display);

    B setLocalized(String localized);

    B setLore(Collection<String> lore);

    B addLoreLine(String loreLine);

    default B addBlankLore() {
        return addLoreLine("");
    }

    B addFlags(ItemFlag... flags);

    default B addFlags(Collection<ItemFlag> flags) {
        return addFlags(flags.toArray(new ItemFlag[0]));
    }

    B setUnbreakable(boolean unbreakable);

}
