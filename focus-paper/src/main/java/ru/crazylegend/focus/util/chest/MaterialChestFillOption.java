package ru.crazylegend.focus.util.chest;

import org.bukkit.Material;

public class MaterialChestFillOption implements ChestFillOption {

    private Material material;

    private MaterialChestFillOption(Material material) {
        this.material = material;
    }

    public static MaterialChestFillOption wrap(Material material) {
        return new MaterialChestFillOption(material);
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public ChestFillOption getOption() {
        return this;
    }
}
