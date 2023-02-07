package ru.crazylegend.focus.command.enhanced.help.line;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.command.enhanced.help.HelpMessageFactory;
import ru.crazylegend.focus.configuration.Messages;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.Arrays;
import java.util.Objects;

/**
 * The builder for a help line
 */
@Setter
public class HelpLineBuilder {

    private final Messages messages;
    private final HelpMessageFactory factory;
    private final HelpLine line;

    private String argumentPathFormat = "help.arguments.%s";
    private String descriptionPathFormat = "help.descriptions.%s";
    private String permissionFormat = "%s";

    /**
     * Creates the new help line builder instance
     *
     * @param messages The messages instance for some messages
     * @param factory  The factory instance for the feedback
     */
    public HelpLineBuilder(@NotNull Messages messages, @NotNull HelpMessageFactory factory) {
        Validate.notNull(messages, "messages");
        Validate.notNull(factory, "factory");
        this.messages = messages;
        this.factory = factory;
        this.line = new HelpLine(messages);
    }

    /**
     * Adds this line to help message using factory feedback
     *
     * @return The original help message factory
     */
    public @NotNull HelpMessageFactory add() {
        factory.addLine(line);
        return factory;
    }

    /**
     * Adds the completed argument for command
     *
     * @param argument The completed argument
     * @return The current builder instance with last changes
     */
    public @NotNull HelpLineBuilder argument(@NotNull String argument) {
        line.addArgument(argument);
        return this;
    }

    /**
     * Adds the completed arguments for command
     *
     * @param arguments The completed arguments array
     * @return The current builder instance with last changes
     */
    public @NotNull HelpLineBuilder arguments(@NotNull String... arguments) {
        line.addArguments(arguments);
        return this;
    }

    /**
     * Adds the argument for command by the messages path
     *
     * @param path The message path with this argument
     * @return The current builder instance with last changes
     * @see HelpMessageFactory#argumentPathFormat(String)
     */
    public @NotNull HelpLineBuilder argumentFrom(@NotNull String path) {
        Validate.notEmpty(path, "path");

        String fullPath = String.format(argumentPathFormat, path);
        String argument = messages.get(fullPath);

        line.addArgument(argument);
        return this;
    }

    /**
     * Adds the arguments for command by the messages sections
     *
     * @param sections The messages sections with this arguments
     * @return The current builder instance with last changes
     * @see HelpMessageFactory#argumentPathFormat(String)
     */
    public @NotNull HelpLineBuilder argumentsFrom(@NotNull String... sections) {
        if (sections == null || sections.length == 0)
            return this;

        Arrays.stream(sections)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .map(s -> String.format(argumentPathFormat, s))
                .map(messages::get)
                .forEach(line::addArgument);

        return this;
    }

    /**
     * Specifies the command for current help line
     *
     * @param command The help line command
     * @return The current builder instance with last changes
     */
    public @NotNull HelpLineBuilder command(@NotNull String command) {
        Validate.notEmpty(command, "command");

        line.setCommand(command);

        String permission = line.getPermission();
        if (permission == null) {
            permission = String.format(permissionFormat, command);
            line.setPermission(permission);
        }

        return this;
    }

    /**
     * Specifies the command for current help line and can auto-generate description & permission
     *
     * @param command                  The help line command
     * @param isDescriptionFromSection Should to auto-generate description & permission or not
     * @return The current builder instance with last changes
     */
    public @NotNull HelpLineBuilder command(@NotNull String command, boolean isDescriptionFromSection) {
        Validate.notEmpty(command, "command");

        line.setCommand(command);

        String description = line.getDescription();
        if (description == null) {
            description = isDescriptionFromSection
                    ? messages.get(String.format(descriptionPathFormat, command))
                    : command;
            line.setDescription(description);
        }

        String permission = line.getPermission();
        if (permission == null) {
            permission = String.format(permissionFormat, command);
            line.setPermission(permission);
        }

        return this;
    }

    /**
     * Specifies the description for the current help line
     *
     * @param description The help line description
     * @return The current builder instance with last changes
     */
    public @NotNull HelpLineBuilder description(@NotNull String description) {
        line.setDescription(description);
        return this;
    }

    /**
     * Specifies the description for the current help line from the messages path
     *
     * @param path The message path with help line description
     * @return The current builder instance with last changes
     * @see HelpMessageFactory#descriptionPathFormat(String)
     */
    public @NotNull HelpLineBuilder descriptionFrom(@NotNull String path) {
        String fullPath = String.format(descriptionPathFormat, path);
        String description = messages.get(fullPath);

        line.setDescription(description);
        return this;
    }

    /**
     * Specifies the main format for the current help line
     *
     * @param format The help line format
     * @return The current builder instance with last changes
     * @see HelpMessageFactory#helpLineFormat(String)
     */
    public @NotNull HelpLineBuilder format(@NotNull String format) {
        line.setFormat(format);
        return this;
    }

    /**
     * Specifies the main format for the current help line by the messages path
     *
     * @param path The full messages path to help line format
     * @return The current builder instance with last changes
     * @see HelpMessageFactory#helpLineFormatFrom(String)
     */
    public @NotNull HelpLineBuilder formatFrom(@NotNull String path) {
        String format = messages.get(path);

        line.setFormat(format);
        return this;
    }

    /**
     * Specifies the required permission for the current help line
     *
     * @param permission The help line required permission
     * @return The current builder instance with last changes
     * @see HelpMessageFactory#permissionFormat(String)
     */
    public @NotNull HelpLineBuilder permission(@NotNull String permission) {
        String formatted = permissionFormat != null
                ? String.format(permissionFormat, permission)
                : permission;

        line.setPermission(formatted);
        return this;
    }

}
