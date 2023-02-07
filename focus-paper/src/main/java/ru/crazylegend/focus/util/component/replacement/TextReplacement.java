package ru.crazylegend.focus.util.component.replacement;

import lombok.Getter;

/**
 * You want to create a new instance?
 * <br>
 * Then you should use {@link Replacements#of(String, Object)}.
 */
@Getter
public class TextReplacement {

    private final String placeholder;
    private final String value;

    TextReplacement(String placeholder, String value) {
        this.placeholder = placeholder;
        this.value = value;
    }

    public String replaceIn(String text) {
        return text.replace(placeholder, value);
    }

}
