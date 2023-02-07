package ru.crazylegend.focus.command.response;


import ru.crazylegend.focus.configuration.Messages;

/**
 * The command response message, okay?
 *
 * @see CommandResponseType
 */
public class CommandResponseMessage {

    private final boolean byKey;
    private final Messages messages;

    private final String key;
    private final String message;

    /**
     * The new command response message instance
     *
     * @param message The command response message
     */
    public CommandResponseMessage(String message) {
        this.byKey = false;
        this.messages = null;

        this.key = null;
        this.message = message;
    }

    /**
     * The new command response message instance
     *
     * @param messageKey    The configuration key of command response message
     * @param configuration The {@link Messages} configuration which contains <b>messageKey</b>
     */
    public CommandResponseMessage(String messageKey, Messages configuration) {
        this.byKey = true;
        this.messages = configuration;

        this.key = messageKey;
        this.message = null;
    }

    /**
     * Gets the message as completed string
     * <p>
     * If it's message by key from messages configuration and this configuration is null, it's will return null
     *
     * @return Completed message (may be null)
     */
    public String getMessage() {
        return byKey
                ? messages != null
                ? messages.get(key)
                : null
                : message;
    }

}
