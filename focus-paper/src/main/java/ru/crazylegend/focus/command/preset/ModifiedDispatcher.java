package ru.crazylegend.focus.command.preset;


import ru.crazylegend.focus.command.enhanced.SubcommandsDispatcher;
import ru.crazylegend.focus.command.response.CommandResponseType;
import ru.crazylegend.focus.configuration.Messages;

/**
 * The simple extension for {@link SubcommandsDispatcher} with the next features..
 * <p>
 * <b>1.</b> The <i>no-args</i> message preset from the <i>error.no-args</i> message section.
 * So, this message preset detects the <i>%command%</i> placeholder and replaces it with
 * received <i>command</i> argument from the constructor.
 * <p>
 * <b>2.</b> The <i>unknown-subcommand</i> message preset from the <i>error.unknown-subcommand</i>
 * message section. So, this message preset also detects the <i>%command%</i> placeholder and
 * replaces it with received <i>command</i> argument from the constructor.
 * <p>
 * Just use this preset for the {@link SubcommandsDispatcher} :D
 */
public abstract class ModifiedDispatcher extends SubcommandsDispatcher {

	public ModifiedDispatcher(String command, Messages messages) {
		super(command, messages);

		String noargs = messages.getFormattedOrDefault("error.no-args", "%command%", command);
		String unknown = messages.getFormattedOrDefault("error.unknown-subcommand", "%command%", command);

		super.setResponseMessage(CommandResponseType.NO_ARGS, noargs);
		super.setResponseMessage(CommandResponseType.UNKNOWN_SUBCOMMAND, unknown);
	}

}
