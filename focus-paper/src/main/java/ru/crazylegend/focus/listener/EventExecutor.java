package ru.crazylegend.focus.listener;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

class EventExecutor<E extends Event> implements org.bukkit.plugin.EventExecutor {

    private Class<E> eventClass;
    private Consumer<E> consumer;

    EventExecutor(Class<E> eventClass, Consumer<E> consumer) {
        this.eventClass = eventClass;
        this.consumer = consumer;
    }

    Class<E> getEventClass() {
        return eventClass;
    }

    @Override
    public void execute(Listener listener, Event event) {
        if (!eventClass.isInstance(event)) {
            return;
        }
        consumer.accept(eventClass.cast(event));
    }
}
