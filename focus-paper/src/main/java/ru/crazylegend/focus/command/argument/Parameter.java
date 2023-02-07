package ru.crazylegend.focus.command.argument;

import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.command.argument.suggestion.SuggestionProvider;

/**
 * Represents the parameter or its alias which can be specified in the command arguments
 *
 * @see ParameterRegistry
 * @since 1.12.0
 */
public interface Parameter {

    /**
     * Create a new parameter instance using this values preset
     *
     * @param parameter          the parameter name
     * @param alias              the parameter alias
     * @param permission         the parameter permission
     * @param suggestionProvider the suggestions provider
     * @param valueRequired      is parameter value required or not
     * @return The created parameter instance
     */
    @NotNull
    static Parameter of(String parameter, String alias, String permission, SuggestionProvider suggestionProvider, boolean valueRequired) {
        return new BaseParameterRegistry.BaseParameter(parameter, alias, permission, suggestionProvider, valueRequired);
    }

    /**
     * Create a new parameter instance using this values preset
     *
     * @param parameter          the parameter name
     * @param alias              the parameter alias
     * @param suggestionProvider the suggestions provider
     * @param valueRequired      is parameter value required or not
     * @return The created parameter instance
     */
    @NotNull
    static Parameter of(String parameter, String alias, SuggestionProvider suggestionProvider, boolean valueRequired) {
        return of(parameter, alias, null, suggestionProvider, valueRequired);
    }

    /**
     * Create a new parameter instance using this values preset
     *
     * @param parameter     the parameter name
     * @param alias         the parameter alias
     * @param valueRequired is parameter value required or not
     * @return The created parameter instance
     */
    @NotNull
    static Parameter of(String parameter, String alias, String permission, boolean valueRequired) {
        return of(parameter, alias, permission, null, valueRequired);
    }

    /**
     * Create a new parameter instance using this values preset
     *
     * @param parameter     the parameter name
     * @param alias         the parameter alias
     * @param valueRequired is parameter value required or not
     * @return The created parameter instance
     */
    @NotNull
    static Parameter of(String parameter, String alias, boolean valueRequired) {
        return of(parameter, alias, null, null, valueRequired);
    }

    /**
     * Create a new parameter instance using this values preset
     *
     * @param parameter the parameter name
     * @param alias     the parameter alias
     * @return The created parameter instance
     */
    @NotNull
    static Parameter of(String parameter, String alias) {
        return of(parameter, alias, false);
    }

    /**
     * Create a new parameter instance using this values preset
     *
     * @param parameter     the parameter name
     * @param valueRequired is parameter value required or not
     * @return The created parameter instance
     */
    @NotNull
    static Parameter of(String parameter, boolean valueRequired) {
        return of(parameter, null, false);
    }

    /**
     * Create a new parameter instance using this values preset
     *
     * @param parameter the parameter name
     * @return The created parameter instance
     */
    @NotNull
    static Parameter of(String parameter) {
        return of(parameter, false);
    }

    /**
     * Get the name of this parameter
     *
     * @return The parameter name
     */
    @NotNull
    String getName();

    /**
     * Get the short parameter a.k.a. parameter alias
     *
     * @return The parameter alias
     */
    @Nullable
    String getShortAlias();

    /**
     * Get the permission that allows to use this parameter
     *
     * @return The parameter permission
     */
    @Nullable
    String getPermission();

    /**
     * Get the suggestions provider of this parameter
     *
     * @return The suggestions provider
     */
    @Nullable
    SuggestionProvider getSuggestionProvider();

    /**
     * Check is this parameter required a value
     *
     * @return 'true' if a value is required or 'false' overwise
     */
    boolean isValueRequired();

    /**
     * Check if this parameter has an alias
     *
     * @return 'true' if this parameter has an alias or 'false' overwise
     * @see #getShortAlias()
     */
    default boolean hasShortAlias() {
        return getShortAlias() != null;
    }

    /**
     * Check if this parameter has a permission
     *
     * @return 'true' if this parameter has a permission or 'false' overwise
     * @see #getPermission()
     */
    default boolean hasPermission() {
        return getPermission() != null;
    }

    /**
     * Check if this parameter has a suggestions provider
     *
     * @return 'true' if this parameter has a suggestions provider or 'false' overwise
     * @see #getSuggestionProvider()
     */
    default boolean hasSuggestionProvider() {
        return getSuggestionProvider() != null;
    }

    /**
     * Check if this permissible (e.g. player) can use this parameter
     *
     * @return 'true' if this permissible can use that or 'false' overwise
     * @see #getPermission()
     * @see #hasPermission()
     */
    default boolean canUse(Permissible who) {
        return !hasPermission() || who.hasPermission(getPermission());
    }

}
