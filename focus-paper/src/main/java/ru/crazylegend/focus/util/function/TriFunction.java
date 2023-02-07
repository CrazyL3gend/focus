package ru.crazylegend.focus.util.function;

import org.apache.commons.lang.Validate;

import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<T, U, K, R> {

    R apply(T t, U u, K k);

    default <V> TriFunction<T, U, K, V> andThen(Function<R, V> after) {
        Validate.notNull(after, "Function is null!");
        return (t, u, k) -> after.apply(apply(t, u, k));
    }

}
