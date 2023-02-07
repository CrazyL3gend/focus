package ru.crazylegend.focus.command.exception;

import lombok.Getter;

@Getter
public class ParameterValueRequiredException extends Exception {

    private final String parameter;

    public ParameterValueRequiredException(String parameter) {
        super("parameter '" + parameter + "' requires a value", null, false, false);
        this.parameter = parameter;
    }

}