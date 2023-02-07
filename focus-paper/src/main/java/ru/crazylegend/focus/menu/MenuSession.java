package ru.crazylegend.focus.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.crazylegend.focus.menu.button.MenuButton;
import ru.crazylegend.focus.menu.filling.FillingStrategy;

import java.util.HashMap;
import java.util.Map;

public class MenuSession implements InventoryHolder {

    private PreparedMenu<?> menu;
    private Player player;
    private Inventory inventory;
    private Map<Integer, MenuButton> buttonMap = new HashMap<>();

    private int page = 0;

    public MenuSession(Player player) {
        this.player = player;
    }

    public int getPage() {
        return page;
    }

    public boolean nextPage() {
        page++;
        update();
        return true;
    }

    public boolean previousPage() {
        if (page == 0) {
            return false;
        }
        page--;
        update();
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public PreparedMenu<?> getMenu() {
        return menu;
    }

    public void setMenu(PreparedMenu<?> menu) {
        this.menu = menu;
    }

    public Map<Integer, MenuButton> getButtonMap() {
        return buttonMap;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void update() {
        inventory.clear();
        FillingStrategy fillingStrategy = menu.getFillingStrategy();
        FillingStrategy.ResultBuilder resultBuilder = FillingStrategy.ResultBuilder.newBuilder();
        fillingStrategy.generate(resultBuilder, this);
        buttonMap = resultBuilder.create();
        buttonMap.forEach((slot, button) -> inventory.setItem(slot, button.getItemStack()));
        player.updateInventory();
    }

    public void setButton(int slot, MenuButton button) {
        buttonMap.put(slot, button);
        inventory.setItem(slot, button.getItemStack());
    }

}
