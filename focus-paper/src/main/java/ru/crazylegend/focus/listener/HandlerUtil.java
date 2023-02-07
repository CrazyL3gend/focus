package ru.crazylegend.focus.listener;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

public final class HandlerUtil {

    private static MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static MethodType handlerListType = MethodType.methodType(HandlerList.class);
    private static Map<Class<? extends Event>, HandlerList> handlerListCache = new HashMap<>();

    private HandlerUtil() {
        throw new UnsupportedOperationException();
    }

    public static HandlerList parseHandlerList(Class<? extends Event> eventClass) {
        try {
            HandlerList list = handlerListCache.get(eventClass);
            if (list == null) {
                list = (HandlerList) lookup.findStatic(eventClass, "getHandlerList", handlerListType).invoke();
                handlerListCache.put(eventClass, list);
            }

            return list;
        } catch (final Throwable throwable) {
            throw new UnsupportedOperationException(throwable);
        }
    }

}
