package ru.crazylegend.focus.util.function;

import org.apache.commons.lang.Validate;

@FunctionalInterface
public interface TriPredicate<T, U, K> {

    boolean test(T t, U u, K k);

    default TriPredicate<T, U, K> negate() {
        return (t, u, k) -> !test(t, u, k);
    }

    default TriPredicate<T, U, K> and(TriPredicate<T, U, K> and) {
        Validate.notNull(and, "Tri predicate is null!");
        return (t, u, k) -> test(t, u, k) && and.test(t, u, k);
    }

    default TriPredicate<T, U, K> or(TriPredicate<T, U, K> or) {
        Validate.notNull(or, "Tri predicate is null!");
        return (t, u, k) -> test(t, u, k) || or.test(t, u, k);
    }

}
