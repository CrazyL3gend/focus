package ru.crazylegend.focus.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import ru.crazylegend.focus.listener.ExtendedListener;

public class FocusMenuService extends ExtendedListener implements MenuService {

    public FocusMenuService(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof MenuSession) {
            MenuSession session = (MenuSession) inventory.getHolder();
            PreparedMenu<?> menu = session.getMenu();
            boolean cancelled = true;

            try {
                cancelled = menu.processClick(session,
                        event.getClick(), event.getSlot());
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                event.setCancelled(cancelled);
            }
        }
    }

    @EventHandler
    public void handlePlayerInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        Inventory topInventory = player.getOpenInventory().getTopInventory();
        if (topInventory.getHolder() instanceof MenuSession && inventory instanceof PlayerInventory) {
            MenuSession session = (MenuSession) topInventory.getHolder();
            PreparedMenu<?> menu = session.getMenu();
            event.setCancelled(menu.isLockUserInventory());

            menu.processPlayerClick(session, event.getClick(), event.getSlot());
        }
    }

    @EventHandler
    public void handleOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof MenuSession) {
            MenuSession session = (MenuSession) inventory.getHolder();
            PreparedMenu<?> menu = session.getMenu();
            menu.processOpen(session);
        }
    }

    @EventHandler
    public void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof MenuSession) {
            MenuSession session = (MenuSession) inventory.getHolder();
            PreparedMenu<?> menu = session.getMenu();
            menu.processClose(session);
        }
    }
}
