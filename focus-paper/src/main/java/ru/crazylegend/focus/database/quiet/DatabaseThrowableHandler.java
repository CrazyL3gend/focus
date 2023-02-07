package ru.crazylegend.focus.database.quiet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class DatabaseThrowableHandler implements ThrowableHandler {

    @NotNull
    private final Logger logger;

    @Override
    public void handle(@NotNull Throwable throwable) {
        logger.severe("An error occurred while trying to execute a database query:");
        logger.severe(throwable.getMessage());
    }

}
