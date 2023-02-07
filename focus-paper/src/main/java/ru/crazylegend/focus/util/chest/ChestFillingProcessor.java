package ru.crazylegend.focus.util.chest;

import com.google.common.collect.Lists;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ChestFillingProcessor {

    private ChestFillingProcessor() {
        throw new UnsupportedOperationException();
    }

    public static ChestFillingResponse fill(Block block, Collection<ItemStack> list, ChestFillOption... optionsArray) {
        Map<ChestFillOption, ChestFillOption> optionMap = new HashMap<>();
        Lists.newArrayList(optionsArray).forEach(option -> optionMap.put(option.getOption(), option));

        Chunk chunk = block.getChunk();
        if (!chunk.isLoaded()) {
            chunk.load();
        }

        Material type = block.getType();
        if (type != Material.CHEST && type != Material.TRAPPED_CHEST && type != Material.ENDER_CHEST) {
            ChestFillOption option = optionMap.get(ChestFillOptions.CREATE_IF_ABSENT);
            if (option == null) {
                return new ChestFillingResponse(block);
            }
            if(option instanceof MaterialChestFillOption) {
                MaterialChestFillOption typeOption = (MaterialChestFillOption) option;
                block.setType(typeOption.getMaterial());
            } else {
                block.setType(Material.CHEST);
            }
        }

        Map<Integer, ItemStack> filledMap = new HashMap<>();
        Inventory inventory = ((Container) block.getState()).getInventory();

        if (optionMap.containsKey(ChestFillOptions.CLEAR_EXISTING)) {
            inventory.clear();
        }

        if (optionMap.containsKey(ChestFillOptions.RANDOMLY_FILL)) {
            list.forEach(item -> {
                int index = ThreadLocalRandom.current().nextInt(0, 26);
                while (inventory.getItem(index) != null) {
                    index = ThreadLocalRandom.current().nextInt(0, 26);
                }

                inventory.setItem(index, item);
            });
        } else {
            int index = 0;
            for (final ItemStack item : list) {
                inventory.setItem(index, item);
                index++;
            }
        }

        return new ChestFillingResponse(block, filledMap);
    }

    public static ChestFillingResponse fill(Location location, Collection<ItemStack> list, ChestFillOption... options) {
        return fill(location.getBlock(), list, options);
    }

}
