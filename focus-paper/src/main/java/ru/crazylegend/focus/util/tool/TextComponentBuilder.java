package ru.crazylegend.focus.util.tool;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The fast way to create extra {@link TextComponent} object
 */
public class TextComponentBuilder {

    private final TextComponent component;
    private final List<BaseComponent> extra;

    /**
     * Creates the new builder for new {@link TextComponent} object
     */
    public TextComponentBuilder() {
        this.component = new TextComponent();
        this.extra = new ArrayList<>();
    }

    /**
     * Adds the extra component from legacy text
     *
     * @param text The legacy text value (may be colorable)
     * @return The current builder instance with last changes
     */
    public TextComponentBuilder addExtra(String text) {
        return addExtra(TextComponent.fromLegacyText(text));
    }

    /**
     * Adds the extra components array as {@link BaseComponent}s array
     *
     * @param extra The components array to add
     * @return The current builder instance with last changes
     */
    public TextComponentBuilder addExtra(BaseComponent... extra) {
        Arrays.stream(extra).forEach(b -> this.extra.add(b));
        return this;
    }

    /**
     * Builds the completed {@link TextComponent} with extra data
     *
     * @return Completed text component
     */
    public TextComponent build() {
        component.setExtra(extra);
        return component;
    }
}