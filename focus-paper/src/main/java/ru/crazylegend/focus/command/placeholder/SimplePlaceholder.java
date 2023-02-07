package ru.crazylegend.focus.command.placeholder;


import ru.crazylegend.focus.configuration.Messages;

/**
 * Simple placeholder object with custom text which extends abstract Placeholder type
 */
public class SimplePlaceholder extends Placeholder {

    /**
     * Placeholder object with custom text
     *
     * @param text - custom placeholder text
     */
    public SimplePlaceholder(String text) {
        super(text);
    }

    /**
     * Placeholder object with text from messages configuration section 'help.placeholders.%name%'
     *
     * @param messages - messages configuration
     * @param name     - name of placeholder, will be used for section
     */
    public SimplePlaceholder(Messages messages, String name) {
        super(messages.get("help.placeholders." + name));
    }

}
