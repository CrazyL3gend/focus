package ru.crazylegend.focus.util.chest;

public enum ChestFillOptions implements ChestFillOption {

    RANDOMLY_FILL,
    CLEAR_EXISTING,
    CREATE_IF_ABSENT;

    @Override
    public ChestFillOptions getOption() {
        return this;
    }

}
