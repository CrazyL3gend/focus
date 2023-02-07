package ru.crazylegend.focus.api.service;

import ru.crazylegend.focus.api.Cancelable;

public interface Service extends Cancelable {

    default void reload() {
    }

}
