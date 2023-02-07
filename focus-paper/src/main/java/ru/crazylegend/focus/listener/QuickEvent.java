package ru.crazylegend.focus.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.TimedRegisteredListener;
import ru.crazylegend.focus.api.Creatable;

import java.util.function.Consumer;

public class QuickEvent<E extends Event> {

    private EventExecutor<E> executor;
    private EventPriority priority;
    private boolean ignoreCancelled;

    private RegisteredListener registeredListener;

    private QuickEvent(EventExecutor<E> executor, EventPriority priority, boolean ignoreCancelled) {
        this.executor = executor;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public static <E extends Event> Builder<E> newBuilder(Class<E> eventClass) {
        return new Builder<>(eventClass);
    }

    public EventPriority getPriority() {
        return priority;
    }

    public boolean ignoreCancelled() {
        return ignoreCancelled;
    }

    EventExecutor<E> getExecutor() {
        return executor;
    }

    RegisteredListener getRegisteredListener(Plugin plugin, Listener listener) {
        if (registeredListener == null) {
            if (Bukkit.getPluginManager().useTimings()) {
                registeredListener = new TimedRegisteredListener(listener, executor, priority, plugin, ignoreCancelled);
            } else {
                registeredListener = new RegisteredListener(listener, executor, priority, plugin, ignoreCancelled);
            }
        }
        return registeredListener;
    }

    public static class Builder<E extends Event> implements Creatable<QuickEvent<E>> {

        private Class<E> eventClass;
        private Consumer<E> action = event -> {
        };
        private EventPriority priority = EventPriority.NORMAL;
        private boolean ignoreCancelled = false;

        private Builder(Class<E> eventClass) {
            this.eventClass = eventClass;
        }

        public Builder<E> addAction(Consumer<E> consumer) {
            action = action.andThen(consumer);
            return this;
        }

        public Builder<E> setPriority(EventPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder<E> setIgnoreCancelled(boolean ignoreCancelled) {
            this.ignoreCancelled = ignoreCancelled;
            return this;
        }

        @Override
        public QuickEvent<E> create() {
            return new QuickEvent<>(new EventExecutor<>(eventClass, event -> action.accept(event)), priority, ignoreCancelled);
        }

    }

}
