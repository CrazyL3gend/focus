package ru.crazylegend.focus.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class BaseDispatchPath implements DispatchPath {

    private final List<String> commands;

    public BaseDispatchPath() {
        this.commands = new ArrayList<>();
    }

    @Override
    public void appendCommand(@NotNull String command) {
        Validate.notEmpty(command, "command");
        commands.add(command);
    }

    @Override
    public @NotNull @UnmodifiableView List<String> getRaw() {
        return Collections.unmodifiableList(commands);
    }

    @Override
    public @NotNull Optional<String> getRootCommand() {
        return getCommandAt(0);
    }

    @Override
    public @NotNull Optional<String> getLastCommand() {
        return getCommandAt(commands.size() - 1);
    }

    @Override
    public @NotNull Optional<String> getCommandAt(int index) {
        return has(index) ? Optional.of(commands.get(index)) : Optional.empty();
    }

    @Override
    public int getLength() {
        return commands.size();
    }

    @Override
    public boolean has(int index) {
        return commands.size() > index;
    }

}
