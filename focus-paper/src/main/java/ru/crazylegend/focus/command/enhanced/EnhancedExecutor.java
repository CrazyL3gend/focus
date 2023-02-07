package ru.crazylegend.focus.command.enhanced;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.command.argument.BaseParameterRegistry;
import ru.crazylegend.focus.command.argument.CommandArguments;
import ru.crazylegend.focus.command.argument.ParameterRegistry;
import ru.crazylegend.focus.command.argument.suggestion.SuggestionResolver;
import ru.crazylegend.focus.command.response.CommandResponseType;
import ru.crazylegend.focus.configuration.Messages;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The enhanced configurable command executor
 */
public abstract class EnhancedExecutor extends ExecutionHelper {

	private final Messages messages;
	private final ParameterRegistry parameterRegistry;
	private final Map<CommandResponseType, String> responses;

	private String permission = null;
	private int requiredArgsCount = 0;
	private boolean playerOnly = false;
	private boolean suggestParameters = true;
	private boolean duplicateSuggestions = false;

	/**
	 * The new enhanced executor instance
	 *
	 * @param messages The messages configuration for some default command response messages
	 */
	public EnhancedExecutor(@NotNull Messages messages) {
		Validate.notNull(messages, "messages");
		this.messages = messages;
		this.parameterRegistry = new BaseParameterRegistry();
		this.responses = new HashMap<>();
	}

	/**
	 * Get the registry of parameters for this command executor
	 *
	 * @return The parameters registry for this executor
	 */
	protected @NotNull ParameterRegistry parameterRegistry() {
		return parameterRegistry;
	}

	/**
	 * Default method to handle the {@link ru.soknight.lib.exception.ParameterValueRequiredException}
	 * <p>Default message section is <b>error.parameter-value-required</b> with placeholder <b>%parameter%</b>
	 *
	 * @param sender    command sender who used this command
	 * @param parameter wrong used parameter
	 */
	protected void onParameterValueUnspecified(@NotNull CommandSender sender, @NotNull String parameter) {
		Validate.notNull(sender, "sender");
		Validate.notEmpty(parameter, "parameter");
		messages.sendFormatted(sender, "error.parameter-value-required", "%parameter%", parameter);
	}

	/**
	 * <b>It's the service method, which will be used by subcommands handler!</b>
	 * <p>
	 * If you want to override execution method, you must use the
	 * {@link EnhancedExecutor#executeCommand(CommandSender, CommandArguments)}
	 *
	 * @param sender The command sender
	 * @param args   The specified command arguments
	 */
	public void validateAndExecute(@NotNull CommandSender sender, @NotNull CommandArguments args) {
		Validate.notNull(sender, "sender");
		Validate.notNull(args, "args");

		// Checks for player-only flag
		if (playerOnly && !isPlayer(sender)) {
			sendResponseMessage(sender, CommandResponseType.ONLY_FOR_PLAYERS);
			return;
		}

		// Checks for permission
		if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
			sendResponseMessage(sender, CommandResponseType.NO_PERMISSIONS);
			return;
		}

		// Checks for arguments count
		if (requiredArgsCount != 0 && requiredArgsCount > args.size()) {
			sendResponseMessage(sender, CommandResponseType.WRONG_SYNTAX);
			return;
		}

		executeCommand(sender, args);
	}

	/**
	 * <b>It's the service method, which will be used by subcommands handler!</b>
	 * <p>
	 * If you want to override execution method, you must use the
	 * {@link EnhancedExecutor#executeTabCompletion(CommandSender, CommandArguments)}
	 *
	 * @param sender The command sender
	 * @param args   The specified command arguments
	 * @return The list of tab-completions as a {@link List} of strings
	 */
	public @Nullable List<String> validateAndTabComplete(@NotNull CommandSender sender, @NotNull CommandArguments args) {
		Validate.notNull(sender, "sender");
		Validate.notNull(args, "args");

		// check for player-only flag
		if (playerOnly && !isPlayer(sender))
			return null;

		// check for permission
		if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission))
			return null;

		List<String> suggestions = executeTabCompletion(sender, args);
		if (suggestions == null)
			suggestions = new ArrayList<>();

		// suggest parameters if needed
		if (suggestParameters && !args.getRaw().isEmpty())
			SuggestionResolver.resolveSuggestions(suggestions, parameterRegistry, sender, args, duplicateSuggestions);

		return suggestions;
	}

	/**
	 * Sends the command response message to command sender
	 * <p>
	 * If this message is not specified, method will send the raw response type enum as string
	 *
	 * @param sender The command sender (receiver for message)
	 * @param type   The type of command response message
	 */
	public void sendResponseMessage(@NotNull CommandSender sender, @NotNull CommandResponseType type) {
		Validate.notNull(sender, "sender");
		Validate.notNull(type, "type");

		String message = responses.get(type);
		String response = message != null ? message : ChatColor.RED + type.toString().toLowerCase();

		sender.sendMessage(response);
	}

	/**
	 * Executes the command for sender
	 * <p>
	 * <u>This method must be overrided</u> by your executor class
	 *
	 * @param sender The command sender
	 * @param args   The specified command arguments
	 */
	protected abstract void executeCommand(@NotNull CommandSender sender, @NotNull CommandArguments args);

	/**
	 * Executes the tab-completion for command sender
	 * <p>
	 * This method must be overrided if you want use custom tab-completions
	 *
	 * @param sender The command sender
	 * @param args   The specified command arguments
	 * @return The list of tab-completions as a {@link List} of strings
	 */
	protected @Nullable List<String> executeTabCompletion(@NotNull CommandSender sender, @NotNull CommandArguments args) {
		return null;
	}

	/**
	 * Gets the current response message for this response type
	 *
	 * @param type The type of command response
	 * @return The current message for this type (may be null)
	 * @see EnhancedExecutor#setResponseMessage(CommandResponseType, String)
	 */
	public @Nullable String getResponseMessage(@NotNull CommandResponseType type) {
		return responses.get(type);
	}

	/**
	 * Sets the response message which was be set for this response type
	 *
	 * @param type    The type of command response
	 * @param message The new response message for this type
	 * @see EnhancedExecutor#getResponseMessage(CommandResponseType)
	 */
	public void setResponseMessage(@NotNull CommandResponseType type, @Nullable String message) {
		Validate.notNull(type, "type");
		responses.put(type, message);
	}

	/**
	 * Sets the response message by key from messages configuration which was be set for this response type
	 * <p>
	 * If specified {@link Messages} configuration in the
	 *
	 * @param type       The type of command response
	 * @param messageKey The key of a new response message for this type
	 * @see EnhancedExecutor#getResponseMessage(CommandResponseType)
	 */
	public void setResponseMessageByKey(@NotNull CommandResponseType type, @NotNull String messageKey) {
		Validate.notNull(type, "type");
		Validate.notEmpty(messageKey, "messageKey");
		responses.put(type, messages != null ? messages.get(messageKey) : null);
	}

	/**
	 * Sets the response message by key from messages configuration which was be set for this response type
	 * <p>
	 * If specified {@link Messages} configuration in the
	 *
	 * @param type         The type of command response
	 * @param messageKey   The key of a new response message for this type
	 * @param replacements The array of message replacements
	 * @see EnhancedExecutor#getResponseMessage(CommandResponseType)
	 * @since 1.12.0
	 */
	public void setResponseMessageByKey(@NotNull CommandResponseType type, @NotNull String messageKey, Object... replacements) {
		Validate.notNull(type, "type");
		Validate.notEmpty(messageKey, "messageKey");
		responses.put(type, messages != null ? messages.getFormatted(messageKey, replacements) : null);
	}

	/**
	 * Gets the used {@link Messages} configuration for some command response messages
	 *
	 * @return The used messages configuration (required value, but may be null)
	 */
	public @NotNull Messages getMessages() {
		return messages;
	}

	/**
	 * Gets the command permission which is required for successfully command execution
	 *
	 * @return The current command permission (optional value, may be null)
	 * @see EnhancedExecutor#setPermission(String)
	 */
	public @Nullable String getPermission() {
		return permission;
	}

	/**
	 * Sets the command permission which is required for successfully command execution
	 *
	 * @param permission The command required permission
	 * @see EnhancedExecutor#getPermission()
	 */
	public void setPermission(@Nullable String permission) {
		this.permission = permission;
	}

	/**
	 * Gets the count of arguments which is required for successfully command execution
	 *
	 * @return The current required arguments count (optional value, is 0 by default)
	 * @see EnhancedExecutor#setRequiredArgsCount(int)
	 */
	public int getRequiredArgsCount() {
		return requiredArgsCount;
	}

	/**
	 * Sets the count of arguments which is required for successfully command execution
	 *
	 * @param requiredArgsCount The command required arguments count
	 * @see EnhancedExecutor#getRequiredArgsCount()
	 */
	public void setRequiredArgsCount(int requiredArgsCount) {
		this.requiredArgsCount = requiredArgsCount;
	}

	/**
	 * Checks if this command currently has flag player-only or not
	 *
	 * @return The player-only flag status (true or false)
	 * @see EnhancedExecutor#setPlayerOnly(boolean)
	 */
	public boolean isPlayerOnly() {
		return playerOnly;
	}

	/**
	 * Sets the player-only flag which determines that this command can be executed only by player
	 *
	 * @param playerOnly The player-only flag value
	 * @see EnhancedExecutor#isPlayerOnly()
	 */
	public void setPlayerOnly(boolean playerOnly) {
		this.playerOnly = playerOnly;
	}

	/**
	 * Check is the parameters will be suggested in the tab completion or not
	 *
	 * @return 'true' if will be or 'false' overwise
	 * @since 1.12.0
	 */
	public boolean doSuggestParameters() {
		return suggestParameters;
	}

	/**
	 * Configure the parameter suggestions for the tab completer
	 * <p>
	 * <b>Default: true</b>
	 *
	 * @param suggestParameters should plugin to suggest parameter names in the tab
	 * @since 1.12.0
	 */
	public void setSuggestParameters(boolean suggestParameters) {
		this.suggestParameters = suggestParameters;
	}

	/**
	 * Configure the parameter suggestions duplication for the tab completer
	 * <p>
	 * <b>Default: false</b>
	 *
	 * @param duplicateSuggestions allow plugin to duplicate parameters suggestions in the tab
	 * @since 1.12.0
	 */
	public void setDuplicateSuggestions(boolean duplicateSuggestions) {
		this.duplicateSuggestions = duplicateSuggestions;
	}

}
