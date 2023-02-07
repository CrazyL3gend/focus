package ru.crazylegend.focus.command.placeholder;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Abstract placeholder type for your custom placeholders
 */
@Getter
@AllArgsConstructor
public abstract class Placeholder {

    private final String text;

}
