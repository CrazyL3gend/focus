package ru.crazylegend.focus.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import ru.crazylegend.focus.annotation.InternalAPI;

import java.util.List;
import java.util.Optional;

public interface DispatchPath {

    @InternalAPI
    void appendCommand(@NotNull String command);

    @NotNull @UnmodifiableView List<String> getRaw();

    @NotNull Optional<String> getRootCommand();

    @NotNull Optional<String> getLastCommand();

    @NotNull Optional<String> getCommandAt(int index);

    int getLength();

    boolean has(int index);

}
