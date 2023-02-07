package ru.crazylegend.focus.api.service;


import ru.crazylegend.focus.api.Cancelable;

public interface FocusServiceApi extends Cancelable {

    default void cancel() {
    }

}
