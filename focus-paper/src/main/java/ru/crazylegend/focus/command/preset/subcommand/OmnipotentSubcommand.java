package ru.crazylegend.focus.command.preset.subcommand;


import ru.crazylegend.focus.command.response.CommandResponseType;
import ru.crazylegend.focus.configuration.Messages;

/**
 * The omnipotent extension for {@link ArgumentableSubcommand} with player-only flag checking :D
 * <p>
 * It's just a checks collection which can save your time!
 * <p>
 * See the sources for additional information and use this preset :D
 */
public abstract class OmnipotentSubcommand extends ArgumentableSubcommand {

    public OmnipotentSubcommand(String parent, String permission, int requiredArgsCount, Messages messages) {
        super(parent, permission, requiredArgsCount, messages);

        super.setPlayerOnly(true);
        super.setResponseMessageByKey(CommandResponseType.ONLY_FOR_PLAYERS, "error.only-for-players");
    }

    public OmnipotentSubcommand(String permission, int requiredArgsCount, Messages messages) {
        this(null, permission, requiredArgsCount, messages);
    }

}
