package ru.crazylegend.focus.menu.reader;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import ru.crazylegend.focus.util.color.ColorUtils;
import ru.crazylegend.focus.util.itemstack.reader.ItemReaders;
import ru.crazylegend.focus.util.itemstack.reader.ReadItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

public final class MenuReaders {

    private static final YamlMenuReaders yamlMenuReaders = new YamlMenuReaders();

    private MenuReaders() {
        throw new UnsupportedOperationException();
    }

    public static YamlMenuReaders yaml() {
        return yamlMenuReaders;
    }

    @Deprecated
    public static YamlMenuReader yamlIndexed() {
        return IndexYamlMenuReader.INST;
    }

    @Deprecated
    public static YamlMenuReader yamlPattern() {
        return PatternYamlMenuReader.INST;
    }

    private enum IndexYamlMenuReader implements YamlMenuReader {

        INST;

        @Override
        public ReadMenu read(ConfigurationSection section) {
            if (section == null) {
                return new ReadMenu(
                        "", 54, InventoryType.CHEST, new HashMap<>(), Collections.emptyList(), new HashMap<>()
                );
            }
            String title = ColorUtils.color(section.getString("title", ""));
            int size = section.getInt("rows", 6) * 9;

            InventoryType inventoryType;
            try {
                inventoryType = section.isString("inventoryType") ? InventoryType.valueOf(section.getString("inventoryType")) : InventoryType.CHEST;
            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
                inventoryType = InventoryType.CHEST;
            }

            Map<Integer, ReadButton> buttonMap = new HashMap<>();

            ConfigurationSection itemSection = section.getConfigurationSection("items");
            if (itemSection != null) {
                itemSection.getKeys(false).forEach(key -> {
                    ConfigurationSection current = itemSection.getConfigurationSection(key);
                    if (current == null) {
                        return;
                    }
                    ReadItem readItem = ItemReaders.yaml().read(current.getConfigurationSection("item"));
                    List<Integer> slotsList = current.getIntegerList("slots");
                    String clickAction = current.getString("click", "NONE");

                    slotsList.forEach(slot -> buttonMap.put(slot, new ReadButton(slot, readItem, clickAction)));
                });
            }

            return new ReadMenu(title, size, inventoryType, buttonMap, Collections.emptyList(), new HashMap<>());
        }
    }

    private enum PatternYamlMenuReader implements YamlMenuReader {

        INST;

        @Override
        public ReadMenu read(ConfigurationSection section) {
            if (section == null) {
                return new ReadMenu("", 54, InventoryType.CHEST, new HashMap<>(), Collections.emptyList(), new HashMap<>());
            }
            String title = ColorUtils.color(section.getString("title", ""));

            InventoryType inventoryType;
            try {
                inventoryType = section.isString("inventoryType") ? InventoryType.valueOf(section.getString("inventoryType")) : InventoryType.CHEST;
            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
                inventoryType = InventoryType.CHEST;
            }

            List<String> patternList = section.getStringList("pattern");
            String[] pattern = patternList.toArray(new String[0]);

            int size = pattern.length * 9;

            Map<Integer, ReadButton> buttonMap = new HashMap<>();
            Map<Character, ReadButton> patternButtonMap = new HashMap<>();

            ConfigurationSection itemSection = section.getConfigurationSection("items");
            if (itemSection != null) {
                itemSection.getKeys(false).forEach(key -> {
                    ConfigurationSection current = itemSection.getConfigurationSection(key);
                    if (current == null) {
                        return;
                    }
                    ReadItem readItem = ItemReaders.yaml().read(current.getConfigurationSection("item"));
                    String clickAction = current.getString("click", "NONE");

                    patternButtonMap.put(key.charAt(0), new ReadButton(readItem, clickAction));
                });
            }

            int slot = 0;
            for (String line : pattern) {
                for (char item : line.toCharArray()) {
                    ReadButton button = patternButtonMap.get(item);
                    if (button != null) {
                        button.setIndex(slot);
                        buttonMap.put(slot, button);
                    }
                    slot++;
                }
            }

            return new ReadMenu(title, size, inventoryType, buttonMap, patternList, patternButtonMap);
        }
    }

    public static class YamlMenuReaders {

        private YamlMenuReaders() {
        }

        public YamlMenuReader indexed() {
            return IndexYamlMenuReader.INST;
        }

        public YamlMenuReader pattern() {
            return PatternYamlMenuReader.INST;
        }

    }

}
