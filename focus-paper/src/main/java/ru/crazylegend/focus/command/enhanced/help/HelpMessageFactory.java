package ru.crazylegend.focus.command.enhanced.help;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import ru.crazylegend.focus.command.enhanced.help.line.HelpLine;
import ru.crazylegend.focus.command.enhanced.help.line.HelpLineBuilder;
import ru.crazylegend.focus.configuration.Messages;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The reloaded factory to create the help message easy
 */
public class HelpMessageFactory {

    private final Messages messages;
    private final HelpMessageCompleter messageCompleter;
    private final List<HelpLine> lines;

    private String argumentPathFormat;
    private String descriptionPathFormat;
    private String helpLineFormat;
    private String permissionFormat;

    /**
     * Creates the new factory instance
     *
     * @param messages The messages instance to getOrDefault some messages
     */
    public HelpMessageFactory(@NotNull Messages messages, @NotNull HelpMessageCompleter messageCompleter) {
        Validate.notNull(messages, "messages");
        Validate.notNull(messageCompleter, "messageCompleter");

        this.messages = messages;
        this.messageCompleter = messageCompleter;
        this.lines = new ArrayList<>();
    }

    /**
     * Adds the specified completed {@link HelpLine} to help message
     *
     * @param line The completed help line
     * @return The current factory instance with last changes
     */
    public @NotNull HelpMessageFactory addLine(@NotNull HelpLine line) {
        Validate.notNull(line, "line");
        lines.add(line);
        return this;
    }

    /**
     * Creates the new line builder and returns it
     *
     * @return The builder of new help line
     */
    public @NotNull HelpLineBuilder newLine() {
        HelpLineBuilder builder = new HelpLineBuilder(messages, this);

        if (argumentPathFormat != null)
            builder.setArgumentPathFormat(argumentPathFormat);

        if (descriptionPathFormat != null)
            builder.setDescriptionPathFormat(descriptionPathFormat);

        if (helpLineFormat != null)
            builder.format(helpLineFormat);

        if (permissionFormat != null)
            builder.setPermissionFormat(permissionFormat);

        return builder;
    }

    /**
     * Gets the help message content as a list of {@link HelpLine}s
     *
     * @return The help message content
     */
    public @NotNull @UnmodifiableView List<HelpLine> getMessageContent() {
        return Collections.unmodifiableList(lines);
    }

    /**
     * Complete the help message. After that any message edits will be unavailable!
     *
     * @return The current factory instance with last changes
     */
    public @NotNull HelpMessageFactory complete() {
        this.messageCompleter.completeMessage();
        return this;
    }

    /**
     * Specifies the format of argument path in your messages.yml file
     * <p>
     * So, if your arguments are located in the <b>help.arguments section</b>, you should
     * to specify <b>help.arguments.%s</b> and next line builders will implement it
     * <p>
     * Default value is: <b>help.arguments.%s</b>
     *
     * @param argumentPathFormat The format of argument path
     * @return The current factory instance with last changes
     */
    public @NotNull HelpMessageFactory argumentPathFormat(@Nullable String argumentPathFormat) {
        this.argumentPathFormat = argumentPathFormat;
        return this;
    }

    /**
     * Specifies the format of description path in your messages.yml file
     * <p>
     * So, if your descriptions are located in the <b>help.descriptions section</b>, you should
     * to specify <b>help.descriptions.%s</b> and next line builders will implement it
     * <p>
     * Default value is: <b>help.descriptions.%s</b>
     *
     * @param descriptionPathFormat The format of description path
     * @return The current factory instance with last changes
     */
    public @NotNull HelpMessageFactory descriptionPathFormat(@Nullable String descriptionPathFormat) {
        this.descriptionPathFormat = descriptionPathFormat;
        return this;
    }

    /**
     * Specifies the format of displayable help line in the game chat
     * <p>
     * This format <b>must contains</b> the <b>%command%</b> and <b>%description%</b> placeholders!
     * <p>
     * Default value is: <b>%command% - %description%</b>
     *
     * @param helpLineFormat The format of displayable help line
     * @return The current factory instance with last changes
     */
    public @NotNull HelpMessageFactory helpLineFormat(@Nullable String helpLineFormat) {
        this.helpLineFormat = helpLineFormat;
        return this;
    }

    /**
     * Specifies the format of displayable help line from the
     * locale {@link Messages} instance by specified path
     * <p>
     * Default value is: <b>%command% - %description%</b>
     *
     * @param path The path in your messages.yml file to this line format
     * @return The current factory instance with last changes
     * @see HelpMessageFactory#helpLineFormat(String)
     */
    public @NotNull HelpMessageFactory helpLineFormatFrom(@NotNull String path) {
        this.helpLineFormat = messages.get(path);
        return this;
    }

    /**
     * Specifies the format of required permission to see the help line
     * <p>
     * By default the factory specifies the permission equals command
     * <p>
     * So, if you creates new line for command 'staff', permission will be 'staff' by default
     * <p>
     * Default value is: <b>%s</b>
     *
     * @param permissionFormat The format of required permission
     * @return The current factory instance with last changes
     */
    public @NotNull HelpMessageFactory permissionFormat(@Nullable String permissionFormat) {
        this.permissionFormat = permissionFormat;
        return this;
    }

}
