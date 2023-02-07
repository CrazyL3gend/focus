package ru.crazylegend.focus.util.inventory;

public enum ChestInventorySize {

    ONE_ROW(1),
    TWO_ROWS(2),
    THREE_ROWS(3),
    FOUR_ROWS(4),
    FIVE_ROWS(5),
    SIX_ROWS(6);

    private int rows;

    ChestInventorySize(int rows) {
        this.rows = rows;
    }

    public static ChestInventorySize fromRows(int rows) {
        for (ChestInventorySize value : values()) {
            if (value.getRows() == rows) {
                return value;
            }
        }
        return null;
    }

    public static ChestInventorySize fromSize(int size) {
        for (ChestInventorySize value : values()) {
            if (value.getSize() == size) {
                return value;
            }
        }
        return null;
    }

    public int getRows() {
        return rows;
    }

    public int getSize() {
        return rows * 9;
    }
}
