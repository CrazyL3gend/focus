package ru.crazylegend.focus.command.preset.standalone;


import ru.crazylegend.focus.command.response.CommandResponseType;
import ru.crazylegend.focus.configuration.Messages;

/**
 * The simple extension for {@link PermissibleCommand} with the arguments count checking..
 * <p>
 * See the sources for additional information and use this preset :D
 */
public abstract class ArgumentableCommand extends PermissibleCommand {

    public ArgumentableCommand(String command, String parent, String permission, int requiredArgsCount, Messages messages) {
        super(command, permission, messages);

        super.setRequiredArgsCount(requiredArgsCount);
        super.setResponseMessageByKey(
                CommandResponseType.WRONG_SYNTAX,
                "error.wrong-syntax",
                "%command%", parent
        );
    }

    public ArgumentableCommand(String command, String permission, int requiredArgsCount, Messages messages) {
        this(command, null, permission, requiredArgsCount, messages);
    }

}
