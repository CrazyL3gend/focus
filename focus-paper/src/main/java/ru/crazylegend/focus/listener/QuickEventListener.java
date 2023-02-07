package ru.crazylegend.focus.listener;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QuickEventListener implements Listener {

    private List<QuickEvent<?>> events = new ArrayList<>();

    private QuickEventListener() {
    }

    public static QuickEventListener newListener() {
        return new QuickEventListener();
    }

    public QuickEventListener event(QuickEvent.Builder<?> builder) {
        events.add(builder.create());
        return this;
    }

    public <E extends Event> QuickEventListener event(Class<E> eventClass, Consumer<E> consumer) {
        return event(QuickEvent.newBuilder(eventClass).addAction(consumer));
    }

    public void register(Plugin plugin) {
        events.forEach(event -> {
            RegisteredListener registeredListener = event.getRegisteredListener(plugin, this);
            Class<? extends Event> eventClass = event.getExecutor().getEventClass();
            HandlerUtil.parseHandlerList(eventClass).register(registeredListener);
        });
    }

}
