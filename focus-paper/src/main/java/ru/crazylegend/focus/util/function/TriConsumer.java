package ru.crazylegend.focus.util.function;

import org.apache.commons.lang.Validate;

@FunctionalInterface
public interface TriConsumer<T, U, K> {

    void accept(T t, U u, K k);

    default TriConsumer<T, U, K> andThen(TriConsumer<? super T, ? super U, ? super K> after) {
        Validate.notNull(after, "Tri consumer is null!");
        return (t, u, k) -> {
            accept(t, u, k);
            after.accept(t, u, k);
        };
    }

}
