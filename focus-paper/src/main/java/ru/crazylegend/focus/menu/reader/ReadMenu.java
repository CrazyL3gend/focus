package ru.crazylegend.focus.menu.reader;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;
import java.util.Map;

public class ReadMenu {

    private String title;
    private int size;
    private InventoryType inventoryType;
    private Map<Integer, ReadButton> buttonMap;

    private Map<Character, ReadButton> patternButtonMap;

    private List<String> matrix;

    ReadMenu(String title, int size, InventoryType inventoryType, Map<Integer, ReadButton> buttonMap, List<String> matrix, Map<Character, ReadButton> patternButtonMap) {
        this.title = title;
        this.size = size;
        this.inventoryType = inventoryType;
        this.buttonMap = buttonMap;
        this.matrix = matrix;
        this.patternButtonMap = patternButtonMap;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public Map<Integer, ReadButton> getButtonMap() {
        return buttonMap;
    }

    public List<String> getMatrix() {
        return matrix;
    }

    public Map<Character, ReadButton> getPatternButtonMap() {
        return patternButtonMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadMenu readMenu = (ReadMenu) o;
        return new EqualsBuilder()
                .append(size, readMenu.size)
                .append(title, readMenu.title)
                .append(inventoryType, readMenu.inventoryType)
                .append(buttonMap, readMenu.buttonMap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(title)
                .append(size)
                .append(inventoryType)
                .append(buttonMap)
                .toHashCode();
    }
}
