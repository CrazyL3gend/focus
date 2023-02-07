package ru.crazylegend.focus.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import ru.crazylegend.focus.api.Creatable;
import ru.crazylegend.focus.menu.button.ClickCallback;
import ru.crazylegend.focus.menu.button.MenuButton;
import ru.crazylegend.focus.menu.filling.FillingStrategy;
import ru.crazylegend.focus.menu.reader.ReadMenu;
import ru.crazylegend.focus.util.color.ColorUtils;
import ru.crazylegend.focus.util.inventory.ChestInventorySize;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PreparedMenu<S extends MenuSession> {

    private static final Function<Player, MenuSession> SESSION_GENERATOR = MenuSession::new;

    private InventoryType type;
    private int size;

    private Function<S, String> titleGenerator;
    private FillingStrategy fillingStrategy;
    private Function<Player, S> sessionGenerator;

    private List<Consumer<S>> openCallbacks;
    private List<Consumer<S>> closeCallbacks;

    private List<ClickCallback> clickCallbacks;

    private boolean lockUserInventory;

    private PreparedMenu(InventoryType type, int size, Function<Player, S> sessionGenerator, Function<S, String> titleGenerator,
                         FillingStrategy fillingStrategy, List<ClickCallback> clickCallbacks, List<Consumer<S>> openCallbacks,
                         List<Consumer<S>> closeCallbacks, boolean lockUserInventory) {
        this.type = type;
        this.size = size;
        this.sessionGenerator = sessionGenerator;
        this.titleGenerator = titleGenerator;
        this.fillingStrategy = fillingStrategy;
        this.clickCallbacks = clickCallbacks;
        this.openCallbacks = openCallbacks;
        this.closeCallbacks = closeCallbacks;
        this.lockUserInventory = lockUserInventory;
    }

    public static <S extends MenuSession> Builder<S> newBuilder() {
        return new Builder<>();
    }

    public InventoryType getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public Function<S, String> getTitleGenerator() {
        return titleGenerator;
    }

    public List<ClickCallback> getClickCallbacks() {
        return clickCallbacks;
    }

    public List<Consumer<S>> getOpenCallbacks() {
        return openCallbacks;
    }

    public List<Consumer<S>> getCloseCallbacks() {
        return closeCallbacks;
    }

    public FillingStrategy getFillingStrategy() {
        return fillingStrategy;
    }

    public boolean isLockUserInventory() {
        return lockUserInventory;
    }

    public MenuSession open(Player player) {
        S session = sessionGenerator.apply(player);
        open(session);
        return session;
    }

    public MenuSession open(S session) {
        Player player = session.getPlayer();
        session.setMenu(this);
        String title = ColorUtils.color(titleGenerator.apply(session));

        Inventory inventory = type == InventoryType.CHEST ? Bukkit.createInventory(session, size, title) : Bukkit.createInventory(session, type, title);
        session.setInventory(inventory);
        session.update();
        player.openInventory(inventory);

        return session;
    }

    boolean processClick(MenuSession session, ClickType type, int slot) {
        MenuButton button = session.getButtonMap().get(slot);
        if (button != null) {
            Player player = session.getPlayer();
            return button.process(player, type, slot);
        }
        return true;
    }

    void processPlayerClick(MenuSession session, ClickType type, int slot) {
        clickCallbacks.forEach(callback -> callback.process(session.getPlayer(), type, slot));
    }

    @SuppressWarnings("unchecked")
    void processOpen(MenuSession session) {
        openCallbacks.forEach(callback -> callback.accept((S) session));
    }

    @SuppressWarnings("unchecked")
    void processClose(MenuSession session) {
        closeCallbacks.forEach(callback -> callback.accept((S) session));
    }

    public static class Builder<S extends MenuSession> implements Creatable<PreparedMenu<S>> {

        private InventoryType type = InventoryType.CHEST;
        private int size = -1;
        @SuppressWarnings("unchecked")
        private Function<Player, S> sessionGenerator = (Function<Player, S>) SESSION_GENERATOR;
        private Function<S, String> titleGenerator = session -> "Инвентарь";
        private FillingStrategy fillingStrategy = (builder, session) -> {
        };
        private List<Consumer<S>> openCallbacks = new ArrayList<>();
        private List<Consumer<S>> closeCallbacks = new ArrayList<>();
        private List<ClickCallback> clickCallbacks = new ArrayList<>();
        private boolean lockUserInventory = false;

        private Builder() {
        }

        public Builder<S> apply(ReadMenu readMenu) {
            return setTitleGenerator(session -> readMenu.getTitle().replace("%player%", session.getPlayer().getName()))
                    .setSize(readMenu.getSize())
                    .setInventoryType(readMenu.getInventoryType());
        }

        public Builder<S> setSessionGenerator(Function<Player, S> sessionGenerator) {
            this.sessionGenerator = sessionGenerator;
            return this;
        }

        public Builder<S> setSize(ChestInventorySize size) {
            return setSize(size.getSize());
        }

        public Builder<S> setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder<S> setInventoryType(InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder<S> setTitleGenerator(Function<S, String> titleGenerator) {
            this.titleGenerator = titleGenerator;
            return this;
        }

        public Builder<S> setFillingStrategy(FillingStrategy fillingStrategy) {
            this.fillingStrategy = fillingStrategy;
            return this;
        }

        public Builder<S> addOpenCallback(Consumer<S> openCallback) {
            openCallbacks.add(openCallback);
            return this;
        }

        public Builder<S> addCloseCallback(Consumer<S> closeCallback) {
            closeCallbacks.add(closeCallback);
            return this;
        }

        public Builder<S> addClickCallback(ClickCallback callback) {
            clickCallbacks.add(callback);
            return this;
        }

        public Builder<S> setLockUserInventory(boolean lockUserInventory) {
            this.lockUserInventory = lockUserInventory;
            return this;
        }

        @Override
        public PreparedMenu<S> create() {
            if (size == -1) {
                size = type.getDefaultSize();
            }
            return new PreparedMenu<>(type, size, sessionGenerator, titleGenerator, fillingStrategy, clickCallbacks, openCallbacks, closeCallbacks, lockUserInventory);
        }
    }

}
