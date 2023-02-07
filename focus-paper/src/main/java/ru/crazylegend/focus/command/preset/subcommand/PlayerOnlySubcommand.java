package ru.crazylegend.focus.command.preset.subcommand;


import ru.crazylegend.focus.command.response.CommandResponseType;
import ru.crazylegend.focus.configuration.Messages;

/**
 * The simple extension for {@link PermissibleSubcommand} with the player-only flag checking..
 * <p>
 * See the sources for additional information and use this preset :D
 */
public abstract class PlayerOnlySubcommand extends PermissibleSubcommand {

    public PlayerOnlySubcommand(String permission, Messages messages) {
        super(permission, messages);

        super.setPlayerOnly(true);
        super.setResponseMessageByKey(CommandResponseType.ONLY_FOR_PLAYERS, "error.only-for-players");
    }

}
