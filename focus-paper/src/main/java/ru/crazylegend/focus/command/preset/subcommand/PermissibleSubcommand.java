package ru.crazylegend.focus.command.preset.subcommand;


import ru.crazylegend.focus.command.enhanced.EnhancedExecutor;
import ru.crazylegend.focus.command.response.CommandResponseType;
import ru.crazylegend.focus.configuration.Messages;

/**
 * The simple extension for {@link EnhancedExecutor} with the permission checking..
 * <p>
 * See the sources for additional information and use this preset :D
 */
public abstract class PermissibleSubcommand extends EnhancedExecutor {

    public PermissibleSubcommand(String permission, Messages messages) {
        super(messages);

        super.setPermission(permission);
        super.setResponseMessageByKey(CommandResponseType.NO_PERMISSIONS, "error.no-permissions");
    }

}
