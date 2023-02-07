package ru.crazylegend.focus.util.inventory;

public final class SlotUtil {

    private SlotUtil() {
        throw new UnsupportedOperationException();
    }

    public static int getSlot(int row, int slot) {
        return ((row - 1) * 9) + slot;
    }

}
