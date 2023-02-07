package ru.crazylegend.focus.util.function;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class Consumers {

    private Consumers() {
        throw new UnsupportedOperationException();
    }

    public static <T> Consumer<T> empty() {
        return t -> {
        };
    }

    public static <T, K> BiConsumer<T, K> emptyBi() {
        return (t, k) -> {
        };
    }
}
