package ru.crazylegend.focus.command.preset.standalone;


import ru.crazylegend.focus.command.enhanced.StandaloneExecutor;
import ru.crazylegend.focus.command.response.CommandResponseType;
import ru.crazylegend.focus.configuration.Messages;

/**
 * The simple extension for {@link StandaloneExecutor} with the permission checking..
 * <p>
 * See the sources for additional information and use this preset :D
 */
public abstract class PermissibleCommand extends StandaloneExecutor {

    public PermissibleCommand(String command, String permission, Messages messages) {
        super(command, messages);

        super.setPermission(permission);
        super.setResponseMessageByKey(CommandResponseType.NO_PERMISSIONS, "error.no-permissions");
    }

}
