package ru.crazylegend.focus.command.enhanced;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.command.argument.CommandArguments;
import ru.crazylegend.focus.command.response.CommandResponseType;
import ru.crazylegend.focus.configuration.Messages;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The dispatcher for subcommands executors which will be added to internal list
 * <p>
 * It's extends the {@link StandaloneExecutor} and therefore you can build the tree from two subcommands
 * executors and more! Just use first subcommands handler as executor of subcommand for second!
 * <p>
 * It's perfectly when you need to add manager via subcommand.
 * For example: <b>/ap manage add|list|remove</b>.
 * Good luck :D
 */
public abstract class SubcommandsDispatcher extends StandaloneExecutor {

    private final Map<String, EnhancedExecutor> executors = new HashMap<>();

    /**
     * The new dispatcher instance
     * <p>
     * <b>NOTICE!</b> Don't forgot about the command response messages!
     * See the {@link CommandResponseType}.
     *
     * @param command  The command name
     * @param messages The messages configuration for some default command response messages
     */
    public SubcommandsDispatcher(@NotNull String command, @NotNull Messages messages) {
        super(command, messages);
    }

    /**
     * Sets the new executor for this subcommand
     * <p>
     * <u>The subcommand string will be in lower case!</u>
     *
     * @param subcommand The subcommand, okay?
     * @param executor   The new subcommand executor
     */
    public void setExecutor(@NotNull String subcommand, @NotNull EnhancedExecutor executor) {
        Validate.notEmpty(subcommand, "subcommand");
        Validate.notNull(executor, "executor");
        executors.put(subcommand.toLowerCase(), executor);
    }

    @Override
    protected void executeCommand(@NotNull CommandSender sender, @NotNull CommandArguments args) {
        if (args.isEmpty()) {
            sendResponseMessage(sender, CommandResponseType.NO_ARGS);
            return;
        }

        String subcommand = args.remove(0);
        if (!executors.containsKey(subcommand.toLowerCase())) {
            sendResponseMessage(sender, CommandResponseType.UNKNOWN_SUBCOMMAND);
            return;
        }

        EnhancedExecutor executor = executors.get(subcommand.toLowerCase());
        args.getDispatchPath().appendCommand(subcommand);

        executor.validateAndExecute(sender, args);
    }

    @Override
    protected @Nullable List<String> executeTabCompletion(@NotNull CommandSender sender, @NotNull CommandArguments args) {
        if (args.isEmpty() || executors.isEmpty())
            return null;

        if (args.size() == 1 && !args.anyParametersFound()) {
            String arg = getLastArgument(args, true);
            return executors.keySet()
                    .stream()
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(arg))
                    .collect(Collectors.toList());
        }

        String subcommand = args.remove(0);
        if (!executors.containsKey(subcommand.toLowerCase()))
            return null;

        args.getDispatchPath().appendCommand(subcommand);
        return executors.get(subcommand.toLowerCase()).validateAndTabComplete(sender, args);
    }

}
