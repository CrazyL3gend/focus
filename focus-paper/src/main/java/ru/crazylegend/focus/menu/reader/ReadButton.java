package ru.crazylegend.focus.menu.reader;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import ru.crazylegend.focus.util.itemstack.reader.ReadItem;

public class ReadButton {

    private int index;
    private ReadItem item;
    private String clickAction;

    ReadButton(int index, ReadItem item, String clickAction) {
        this.index = index;
        this.item = item;
        this.clickAction = clickAction;
    }

    ReadButton(ReadItem item, String clickAction) {
        this.item = item;
        this.clickAction = clickAction;
    }

    public int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    public ReadItem getItem() {
        return item;
    }

    public String getClickAction() {
        return clickAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadButton that = (ReadButton) o;
        return new EqualsBuilder()
                .append(index, that.index)
                .append(item, that.item)
                .append(clickAction, that.clickAction)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(index)
                .append(item)
                .append(clickAction)
                .toHashCode();
    }
}
