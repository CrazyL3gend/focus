package ru.crazylegend.focus.command.argument;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a null-safe parameter value providing methods
 * to get fast access to the value and transform it to needed type
 *
 * @since 1.12.0
 */
public interface ParameterValue {

    /**
     * Check is this instance has a value (value != null)
     *
     * @return 'true' if this instance has a value or 'false' overwise
     */
    boolean hasValue();

    /**
     * Check is this instance has a value and it's not empty string
     *
     * @return 'true' if this instance has a non-empty value or 'false' overwise
     */
    boolean isNonEmpty();

    /**
     * Get original value that stored in this instance currently
     *
     * @return The original value as string
     */
    @Nullable
    String asString();

    /**
     * Get original value that stored in this instance currently
     *
     * @param def default value to return if the original value will be null
     * @return The original value as string if exists or the default value overwise
     */
    @Nullable
    String asString(@Nullable String def);

    /**
     * Try to get original value as boolean
     *
     * @return Successfully transformed original value as boolean or 'null' overwise
     */
    @Nullable
    Boolean asBoolean();

    /**
     * Try to get original value as boolean
     *
     * @param def default value to return if the original value will be null
     * @return Successfully transformed original value as boolean or the default value overwise
     */
    boolean asBoolean(boolean def);

    /**
     * Try to get original value as integer
     *
     * @return Successfully transformed original value as integer or 'null' overwise
     */
    @Nullable
    Integer asInt();

    /**
     * Try to get original value as integer
     *
     * @param def default value to return if the original value will be null
     * @return Successfully transformed original value as integer or the default value overwise
     */
    int asInt(int def);

    /**
     * Try to get original value as double
     *
     * @return Successfully transformed original value as double or 'null' overwise
     */
    @Nullable
    Double asDouble();

    /**
     * Try to get original value as double
     *
     * @param def default value to return if the original value will be null
     * @return Successfully transformed original value as double or the default value overwise
     */
    double asDouble(double def);

    /**
     * Try to get original value as float
     *
     * @return Successfully transformed original value as float or 'null' overwise
     */
    @Nullable
    Float asFloat();

    /**
     * Try to get original value as float
     *
     * @param def default value to return if the original value will be null
     * @return Successfully transformed original value as float or the default value overwise
     */
    float asFloat(float def);

    /**
     * Try to get original value as long
     *
     * @return Successfully transformed original value as long or 'null' overwise
     */
    @Nullable
    Long asLong();

    /**
     * Try to get original value as long
     *
     * @param def default value to return if the original value will be null
     * @return Successfully transformed original value as long or the default value overwise
     */
    long asLong(long def);

}
