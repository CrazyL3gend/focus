package ru.crazylegend.focus.util.chest;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ChestFillingResponse {

    private final Block block;
    private final Map<Integer, ItemStack> filledMap;
    private final boolean filled;

    ChestFillingResponse(Block block, Map<Integer, ItemStack> filledMap) {
        this.block = block;
        this.filledMap = filledMap;
        this.filled = true;
    }

    ChestFillingResponse(Block block) {
        this.block = block;
        this.filledMap = new HashMap<>();
        this.filled = false;
    }

    public boolean isFilled() {
        return filled;
    }

    public Block getBlock() {
        return block;
    }

    public Map<Integer, ItemStack> getFilled() {
        return filledMap;
    }


}
