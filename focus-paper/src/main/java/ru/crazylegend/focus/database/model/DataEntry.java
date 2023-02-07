package ru.crazylegend.focus.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "focus_data")
public final class DataEntry {

    @DatabaseField(columnName = "key", id = true, canBeNull = false)
    private @NotNull String key;

    @DatabaseField(columnName = "value", canBeNull = false)
    private @NotNull String value;

    public DataEntry(@NotNull String key, @NotNull Object value) {
        Validate.notEmpty(key, "key");
        Validate.notNull(value, "value");
        this.key = key;
        this.value = value.toString();
    }

    public @NotNull OptionalInt getValueAsInt() {
        return wrapValue(Integer::parseInt, OptionalInt::of, OptionalInt::empty);
    }

    public @NotNull OptionalLong getValueAsLong() {
        return wrapValue(Long::parseLong, OptionalLong::of, OptionalLong::empty);
    }

    public @NotNull OptionalDouble getValueAsDouble() {
        return wrapValue(Double::parseDouble, OptionalDouble::of, OptionalDouble::empty);
    }

    public void setValue(@NotNull Object value) {
        Validate.notNull(value, "value");
        this.value = value.toString();
    }

    private <T, W> W wrapValue(
            @NotNull Function<String, T> mapper,
            @NotNull Function<T, W> notNullWrapper,
            @NotNull Supplier<W> nullWrapper
    ) {
        try {
            T mappedValue = mapper.apply(value);
            return mappedValue != null ? notNullWrapper.apply(mappedValue) : nullWrapper.get();
        } catch (Throwable ignored) {
            return nullWrapper.get();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataEntry dataEntry = (DataEntry) o;
        return key.equals(dataEntry.key) &&
                value.equals(dataEntry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public @NotNull String toString() {
        return "DataEntry{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    private interface ValueWrapper<T> {

        @NotNull T wrap(@NotNull String value) throws NumberFormatException;

    }

}
