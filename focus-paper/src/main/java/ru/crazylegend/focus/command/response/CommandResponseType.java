package ru.crazylegend.focus.command.response;

import ru.crazylegend.focus.command.enhanced.EnhancedExecutor;

/**
 * The type of command response for (sub)commands executors and subcommands handler
 *
 * @see CommandResponseMessage
 * @see EnhancedExecutor#setResponseMessage(CommandResponseType, String)
 */
public enum CommandResponseType {

    /**
     * <b>For (sub)command executor only!</b>
     * <p>
     * This message will be sent when the command sender will not has required permission to use this command
     * <p>
     * For example, if you have <u>(sub)command executor</u> with <b>ap.help</b> permission (see the setter below)
     * and this command will be called by someone player without this permission then this executor will return
     * this response message as an error
     *
     * @see EnhancedExecutor#setPermission(String)
     * @see EnhancedExecutor#setResponseMessage(CommandResponseType, String)
     */
    NO_PERMISSIONS,

    /**
     * <b>For (sub)command executor only!</b>
     * <p>
     * This message will be sent when the command sender will specify an insufficient count of arguments
     * <p>
     * For example, if you have <u>(sub)command executor</u> with <b>3</b> required arguments (see the setter below)
     * and this command will be called with <b>2</b> command arguments (that is less than required) then this
     * executor will return this response message as an error
     *
     * @see EnhancedExecutor#setRequiredArgsCount(int)
     * @see EnhancedExecutor#setResponseMessage(CommandResponseType, String)
     */
    WRONG_SYNTAX,

    /**
     * <b>For (sub)command executor only!</b>
     * <p>
     * This message will be sent when the <u>player-only</u> flagged command will be called by non-player sender
     * <p>
     * For example, if you have <u>(sub)command executor</u> with active player-only flag (see the setter below)
     * and this command will be called from the console, it's will return this response message as an error
     *
     * @see EnhancedExecutor#setPlayerOnly(boolean)
     * @see EnhancedExecutor#setResponseMessage(CommandResponseType, String)
     */
    ONLY_FOR_PLAYERS,

    /**
     * <b>For subcommands handler only!</b>
     * <p>
     * This message will be sent when the <u>subcommands handler</u> will be triggered without command arguments
     * <p>
     * For example, if you have <u>subcommands handler</u> for main plugin command such as '/ap <subcommand>' and
     * someone player will execute this command without subcommand argument, he will receive this message as an error
     *
     * @see EnhancedExecutor#setResponseMessage(CommandResponseType, String)
     */
    NO_ARGS,

    /**
     * <b>For subcommands handler only!</b>
     * <p>
     * This message will be sent when the <u>subcommands handler</u> will be triggered with unknown subcommand
     * <p>
     * For example, if you have <u>subcommands handler</u> for main plugin command such as '/ap <subcommand>' and
     * it's executes specified subcommands such as 'help' and 'reload', other unhandled subcommands will
     * be considered as unknown subcommands and the caller will receive this message as an error
     *
     * @see EnhancedExecutor#setResponseMessage(CommandResponseType, String)
     */
    UNKNOWN_SUBCOMMAND;

}
