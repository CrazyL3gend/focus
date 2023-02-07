package ru.crazylegend.focus.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.command.argument.suggestion.SuggestionProvider;

/**
 * The builder for the {@link Parameter} class
 *
 * @since 1.12.0
 */
public interface ParameterBuilder {

    /**
     * Get current parameter instance
     *
     * @return Current parameter
     */
    @NotNull
    Parameter getParameter();

    /**
     * Get a permission mask provided by the registry
     *
     * @return The permission mask
     */
    @Nullable
    String getPermissionMask();

    /**
     * Add current parameter instance to the {@link ParameterRegistry}
     *
     * @return The parameter registry to continue the method chaining
     * @throws UnsupportedOperationException when a parameter registry was not
     *                                       specified during a builder instance initialization
     */
    @NotNull
    ParameterRegistry addToRegistry();

    /**
     * Specify a new name for current parameter instance
     *
     * @param name a new parameter name
     * @return Current builder instance to continue the method chaining
     * @see Parameter#getName()
     */
    ParameterBuilder name(@NotNull String name);

    /**
     * Specify a new short alias for current parameter instance
     *
     * @param shortAlias a new short alias value
     * @return Current builder instance to continue the method chaining
     * @see Parameter#getShortAlias()
     */
    ParameterBuilder shortAlias(String shortAlias);

    /**
     * Specify a new permission for current parameter instance
     *
     * @param permission a new permission value
     * @return Current builder instance to continue the method chaining
     * @see Parameter#getPermission()
     */
    ParameterBuilder permission(String permission);

    /**
     * Specify a new suggestion provider for current parameter instance
     *
     * @param suggestionProvider a new suggestion provider instance
     * @return Current builder instance to continue the method chaining
     * @see Parameter#getSuggestionProvider()
     */
    ParameterBuilder suggestionProvider(SuggestionProvider suggestionProvider);

    /**
     * Specify the value-required flag for current parameter instance
     *
     * @param valueRequired a new value for this flag
     * @return Current builder instance to continue the method chaining
     * @see Parameter#isValueRequired()
     */
    ParameterBuilder requiresValue(boolean valueRequired);

    /**
     * Enable the value-required flag for current parameter instance
     *
     * @return Current builder instance to continue the method chaining
     * @see #requiresValue(boolean)
     */
    default ParameterBuilder requiresValue() {
        return requiresValue(true);
    }

}
