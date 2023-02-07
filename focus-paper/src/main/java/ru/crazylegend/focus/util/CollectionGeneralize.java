package ru.crazylegend.focus.util;

import java.util.*;

@SuppressWarnings("unchecked cast")
public final class CollectionGeneralize {

    private CollectionGeneralize() {
        throw new UnsupportedOperationException();
    }

    public static <T> Collection<T> generalizeCollection(Collection<? extends T> collection) {
        return (Collection<T>) collection;
    }

    public static <T> List<T> generalizeList(List<? extends T> collection) {
        return (List<T>) collection;
    }

    public static <T> Set<T> generalizeSet(Set<? extends T> collection) {
        return (Set<T>) collection;
    }

    public static <T> SortedSet<T> generalizeSortedSet(SortedSet<? extends T> collection) {
        return (SortedSet<T>) collection;
    }

    public static <T> NavigableSet<T> generalizeNavigableSet(NavigableSet<? extends T> collection) {
        return (NavigableSet<T>) collection;
    }

    public static <T> Queue<T> generalizeQueue(Queue<? extends T> collection) {
        return (Queue<T>) collection;
    }

    public static <T> Deque<T> generalizeQueue(Deque<? extends T> collection) {
        return (Deque<T>) collection;
    }

}
