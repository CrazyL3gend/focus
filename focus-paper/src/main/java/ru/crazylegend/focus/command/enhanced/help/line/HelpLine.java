package ru.crazylegend.focus.command.enhanced.help.line;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.configuration.Messages;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
public class HelpLine {

    private final Messages messages;
    private final StringBuilder arguments;

    private String command;
    private String description;
    private String format;
    private String permission;

    public HelpLine(@NotNull Messages messages) {
        Validate.notNull(messages, "messages");
        this.messages = messages;
        this.arguments = new StringBuilder();
        this.format = "%command% - %description%";
    }

    public void addArgument(@NotNull String argument) {
        if (argument != null && !argument.isEmpty())
            arguments.append(" ").append(argument);
    }

    public void addArguments(@NotNull String... arguments) {
        if (arguments != null)
            Arrays.stream(arguments).forEach(this::addArgument);
    }

    public @NotNull String format() {
        if (format == null)
            return ChatColor.RED + "Help line format is null :(";

        String command = this.command != null ? this.command : "";
        String description = this.description != null ? this.description : "";

        if (arguments.length() != 0)
            command += arguments.toString();

        return messages.format(format, "%command%", command, "%description%", description);
    }

    public boolean isAvailableFor(@NotNull CommandSender sender) {
        return sender != null && (permission == null || permission.isEmpty() || sender.hasPermission(permission));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HelpLine helpLine = (HelpLine) o;
        return Objects.equals(command, helpLine.command) &&
                Objects.equals(description, helpLine.description) &&
                Objects.equals(format, helpLine.format) &&
                Objects.equals(permission, helpLine.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, description, format, permission);
    }

    @Override
    public @NotNull String toString() {
        return "HelpLine{" +
                "command='" + command + '\'' +
                ", description='" + description + '\'' +
                ", format='" + format + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }

}
