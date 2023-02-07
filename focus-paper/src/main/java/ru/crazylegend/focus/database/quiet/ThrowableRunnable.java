package ru.crazylegend.focus.database.quiet;

@FunctionalInterface
public interface ThrowableRunnable {

    void run() throws Throwable;

}
