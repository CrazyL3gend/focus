package ru.crazylegend.focus.database.quiet;

@FunctionalInterface
public interface ThrowableSupplier<T> {

    T supply() throws Throwable;

}
