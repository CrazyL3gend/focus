package ru.crazylegend.focus.util.component.replacement;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import ru.crazylegend.focus.util.component.injection.TextComponentInjector;

/**
 * You want to create a new instance?
 * <br>
 * Then you should use
 */
@Getter
public class TextComponentReplacement {

    private final String placeholder;
    private final TextComponent value;

    TextComponentReplacement(String placeholder, TextComponent value) {
        this.placeholder = placeholder;
        this.value = value;
    }

    private static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public TextComponent injectIn(String text) {
        return TextComponentInjector.inject(text, placeholder, value);
    }

    public TextComponentReplacement onClick(ClickEvent.Action action, String value) {
        this.value.setClickEvent(new ClickEvent(action, value));
        return this;
    }

    public TextComponentReplacement onClickChangePage(int pageNumber) {
        return onClick(ClickEvent.Action.CHANGE_PAGE, String.valueOf(pageNumber));
    }

    public TextComponentReplacement onClickOpenUrl(String url) {
        return onClick(ClickEvent.Action.OPEN_URL, url);
    }

    public TextComponentReplacement onClickRunCommand(String command) {
        return onClick(ClickEvent.Action.RUN_COMMAND, command);
    }

    public TextComponentReplacement onClickSuggestCommand(String command) {
        return onClick(ClickEvent.Action.SUGGEST_COMMAND, command);
    }

    public TextComponentReplacement onHover(HoverEvent.Action action, BaseComponent[] components) {
        this.value.setHoverEvent(new HoverEvent(action, components));
        return this;
    }

}