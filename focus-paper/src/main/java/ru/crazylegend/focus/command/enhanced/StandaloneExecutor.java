package ru.crazylegend.focus.command.enhanced;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.command.MergedExecutor;
import ru.crazylegend.focus.command.argument.BaseCommandArguments;
import ru.crazylegend.focus.command.argument.CommandArguments;
import ru.crazylegend.focus.command.exception.ParameterValueRequiredException;
import ru.crazylegend.focus.configuration.Messages;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.List;

/**
 * <u>Use it to create a standalone command in your plugin</u>
 * <p>
 * The standalone executor implementation for {@link MergedExecutor}
 * <p>
 * So it's a child executor class of {@link EnhancedExecutor} and it implements all methods from parent class
 */
public abstract class StandaloneExecutor extends EnhancedExecutor implements MergedExecutor {

	private final String command;

	/**
	 * The new standalone executor instance
	 *
	 * @param command  The command name
	 * @param messages The messages configuration for some default command response messages
	 */
	public StandaloneExecutor(@NotNull String command, @NotNull Messages messages) {
		super(messages);

		Validate.notEmpty(command, "command");
		this.command = command;
	}

	/**
	 * Registers this class as executor for plugin's command
	 * <p>
	 * So by default this class <u>will not be registered as tab-completer</u>.
	 * If you want it, you need to use {@link StandaloneExecutor#register(JavaPlugin, boolean)}
	 * and send 'true' boolean parameter to register the tab-completer
	 * <p>
	 * If the internal <u>command name is null</u> or <u>plugin is null</u> or
	 * <u>plugin hasn't this command</u>, registration of this class will be aborted
	 *
	 * @param plugin The plugin which is owner of command
	 */
	public void register(@NotNull JavaPlugin plugin) {
		register(plugin, false);
	}

	/**
	 * Registers this class as executor and tab-completer (if needed) for plugin's command
	 * <p>
	 * If the internal <u>command name is null</u> or <u>plugin is null</u> or
	 * <u>plugin hasn't this command</u>, registration of this class will be aborted
	 *
	 * @param plugin               The plugin which is owner of command
	 * @param registerTabCompleter Should this class will be registered as tab-completer or not
	 */
	public void register(@NotNull JavaPlugin plugin, boolean registerTabCompleter) {
		Validate.notNull(plugin, "plugin");

		PluginCommand command = plugin.getCommand(this.command);
		if (command != null) {
			command.setExecutor(this);

			if (registerTabCompleter) {
				command.setTabCompleter(this);
			}
		}
	}

	@Override
	protected abstract void executeCommand(@NotNull CommandSender sender, @NotNull CommandArguments args);

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		try {
			CommandArguments arguments = new BaseCommandArguments(sender, args, parameterRegistry());
			arguments.getDispatchPath().appendCommand(getCommand());

			return validateAndTabComplete(sender, arguments);
		} catch (ParameterValueRequiredException ignored) {
			return null;
		}
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		try {
			CommandArguments arguments = new BaseCommandArguments(sender, args, parameterRegistry());
			arguments.getDispatchPath().appendCommand(getCommand());

			validateAndExecute(sender, arguments);
			return true;
		} catch (ParameterValueRequiredException ex) {
			onParameterValueUnspecified(sender, ex.getParameter());
			return true;
		}
	}

	/**
	 * Gets the command name which was be specified by the constructor
	 *
	 * @return The command name (required value, but may be null)
	 */
	public @NotNull String getCommand() {
		return command;
	}

}
