package ru.crazylegend.focus.command.argument;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * The internal string-based {@link ParameterValue} implementation
 *
 * @since 1.12.0
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class BaseParameterValue implements ParameterValue {

    static final ParameterValue EMPTY = new BaseParameterValue(null);

    private final String value;

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Override
    public boolean isNonEmpty() {
        return hasValue() && !value.isEmpty();
    }

    @Override
    public @Nullable String asString() {
        return value;
    }

    @Override
    public @Nullable String asString(@Nullable String def) {
        return value != null ? value : def;
    }

    @Override
    public @Nullable Boolean asBoolean() {
        if (value == null)
            return null;

        if (value.equalsIgnoreCase("true"))
            return true;
        else if (value.equalsIgnoreCase("false"))
            return false;
        else
            return null;
    }

    @Override
    public boolean asBoolean(boolean def) {
        Boolean asBoolean = asBoolean();
        return asBoolean != null ? asBoolean : def;
    }

    @Override
    public @Nullable Integer asInt() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public int asInt(int def) {
        Integer asInt = asInt();
        return asInt != null ? asInt : def;
    }

    @Override
    public @Nullable Double asDouble() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public double asDouble(double def) {
        Double asDouble = asDouble();
        return asDouble != null ? asDouble : def;
    }

    @Override
    public @Nullable Float asFloat() {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public float asFloat(float def) {
        Float asFloat = asFloat();
        return asFloat != null ? asFloat : def;
    }

    @Override
    public @Nullable Long asLong() {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public long asLong(long def) {
        Long asLong = asLong();
        return asLong != null ? asLong : def;
    }

    @Override
    public String toString() {
        return value;
    }

}
