package ru.crazylegend.focus.command.argument;

import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.command.argument.suggestion.SuggestionProvider;

import java.util.List;

/**
 * Represents the registry of parameters which can be
 * correctly resolved in the command arguments
 *
 * @since 1.12.0
 */
public interface ParameterRegistry {

    /***********************************
     *                                 *
     *    REGISTRY SETTINGS METHODS    *
     *                                 *
     **********************************/

    /**
     * Check is this registry saving unknown found parameters
     *
     * @return The flag value
     * @see #saveUnknownParameters(boolean)
     */
    boolean isSavingUnknownParameters();

    /**
     * Configure unknown parameters saving option for this registry
     *
     * @param value new flag value
     * @return Current registry instance to continue method chaining
     * @see #isSavingUnknownParameters()
     */
    ParameterRegistry saveUnknownParameters(boolean value);

    /**
     * Get a permission mask used by this registry
     *
     * @return The permission mask
     * @see #setPermissionMask(String)
     */
    @Nullable
    String getPermissionMask();

    /**
     * Set a permission mask for this registry
     * <p>
     * <h2>What is that?</h2>
     * When you will create a new parameter builder instance, registry will tell this builder
     * that you want to set permissions for new parameters automaticaly. Your mask <b>should
     * have the %s placeholder</b> if you want to use a parameter name in the permission, but
     * it is only a useful feature, not a requirement!
     * </p>
     * <p/>
     * <p>
     * <b>Example:</b> The mask <i>sklibrary.command.parameter.%s</i> for parameter <i>silent</i>
     * will set permission <i>sklibrary.command.parameter.silent</i> automaticaly.
     * </p>
     *
     * @param mask new permission mask
     * @return Current registry instance to continue method chaining
     * @see #getPermissionMask()
     */
    ParameterRegistry setPermissionMask(String mask);

    /***************************************
     *                                     *
     *    FAST-CREATE PARAMETER METHODS    *
     *                                     *
     **************************************/

    /**
     * Create a new parameter builder instance to build new parameter
     *
     * @return A new instance of {@link ParameterBuilder}
     */
    ParameterBuilder create();

    /**
     * Create a new parameter builder with default values preset
     *
     * @param parameter default 'full parameter' builder value
     * @return A new instance of {@link ParameterBuilder} with values preset
     */
    default ParameterBuilder create(String parameter) {
        return create(parameter, false);
    }

    /**
     * Create a new parameter builder with default values preset
     *
     * @param parameter     default 'full parameter' builder value
     * @param valueRequired default 'value required' builder value
     * @return A new instance of {@link ParameterBuilder} with values preset
     */
    default ParameterBuilder create(String parameter, boolean valueRequired) {
        return create().name(parameter).requiresValue(valueRequired);
    }

    /**
     * Create a new parameter builder with default values preset
     *
     * @param parameter default 'full parameter' builder value
     * @param alias     default 'short alias' builder value
     * @return A new instance of {@link ParameterBuilder} with values preset
     */
    default ParameterBuilder create(String parameter, String alias) {
        return create(parameter, alias, false);
    }

    /**
     * Create a new parameter builder with default values preset
     *
     * @param parameter     default 'full parameter' builder value
     * @param alias         default 'short alias' builder value
     * @param valueRequired default 'value required' builder value
     * @return A new instance of {@link ParameterBuilder} with values preset
     */
    default ParameterBuilder create(String parameter, String alias, boolean valueRequired) {
        return create(parameter, alias, null, valueRequired);
    }

    /**
     * Create a new parameter builder with default values preset
     *
     * @param parameter          default 'full parameter' builder value
     * @param alias              default 'short alias' builder value
     * @param suggestionProvider default 'suggestion provider' builder value
     * @param valueRequired      default 'value required' builder value
     * @return A new instance of {@link ParameterBuilder} with values preset
     */
    default ParameterBuilder create(String parameter, String alias, SuggestionProvider suggestionProvider, boolean valueRequired) {
        return create()
                .name(parameter)
                .shortAlias(alias)
                .suggestionProvider(suggestionProvider)
                .requiresValue(valueRequired);
    }

    /************************************
     *                                  *
     *    FAST-ADD PARAMETER METHODS    *
     *                                  *
     ***********************************/

    /**
     * Add a new parameter to this registry
     *
     * @param parameter parameter instance to add
     * @return The previously stored parameter with same name (optional)
     */
    @Nullable
    Parameter add(Parameter parameter);

    /**
     * Add a new parameter from values preset to this registry
     *
     * @param parameter the 'full parameter' value of preset
     * @return The previously stored parameter with same name (optional)
     */
    @Nullable
    default Parameter add(String parameter) {
        return add(parameter, false);
    }

    /**
     * Add a new parameter from values preset to this registry
     *
     * @param parameter     the 'full parameter' value of preset
     * @param valueRequired the 'value required' value of preset
     * @return The previously stored parameter with same name (optional)
     */
    @Nullable
    default Parameter add(String parameter, boolean valueRequired) {
        return add(parameter, null, valueRequired);
    }

    /**
     * Add a new parameter from values preset to this registry
     *
     * @param parameter the 'full parameter' value of preset
     * @param alias     the 'short alias' value of preset
     * @return The previously stored parameter with same name (optional)
     */
    @Nullable
    default Parameter add(String parameter, String alias) {
        return add(parameter, alias, false);
    }

    /**
     * Add a new parameter from values preset to this registry
     *
     * @param parameter     the 'full parameter' value of preset
     * @param alias         the 'short alias' value of preset
     * @param valueRequired the 'value required' value of preset
     * @return The previously stored parameter with same name (optional)
     */
    @Nullable
    default Parameter add(String parameter, String alias, boolean valueRequired) {
        return add(parameter, alias, null, valueRequired);
    }

    /**
     * Add a new parameter from values preset to this registry
     *
     * @param parameter          the 'full parameter' value of preset
     * @param alias              the 'short alias' value of preset
     * @param suggestionProvider the 'suggestion provider' value of preset
     * @param valueRequired      the 'value required' value of preset
     * @return The previously stored parameter with same name (optional)
     */
    @Nullable
    default Parameter add(String parameter, String alias, SuggestionProvider suggestionProvider, boolean valueRequired) {
        return add(create(parameter, alias, suggestionProvider, valueRequired).getParameter());
    }

    /*************************
     *                       *
     *    GENERAL METHODS    *
     *                       *
     ************************/

    /**
     * Get the stored parameter instance by its name
     *
     * @param parameter name of parameter to get from
     * @return The stored parameter with this name (nullable)
     */
    @Nullable
    Parameter get(String parameter);

    /**
     * Get the stored parameter instance by its short alias
     *
     * @param alias short alias of parameter to get from
     * @return The stored parameter with this alias (nullable)
     */
    @Nullable
    Parameter getByAlias(String alias);

    /**
     * Get list copy of all parameters stored from this registry
     *
     * @return A new list copy of all stored parameters
     */
    List<Parameter> getAllRegistered();

    /**
     * Get list copy of all registered parameters from this registry
     *
     * @return A new list copy of all registered parameters
     */
    List<String> getRegisteredParameters();

    /**
     * Get list copy of all registered parameter aliases from this registry
     *
     * @return A new list copy of all registered parameter aliases
     */
    List<String> getRegisteredAliases();

    /**
     * Check is this registry contains parameter with this name
     *
     * @param parameter name of parameter to check
     * @return 'true' if this registry contains parameter with
     * this name or 'false' overwise
     */
    boolean has(String parameter);

    /**
     * Check is this registry contains parameter with this name or with this alias
     *
     * @param parameter name of parameter to check
     * @param alias     alias of parameter to check
     * @return 'true' if this registry contains parameter with
     * this name/alias or 'false' overwise
     */
    boolean has(String parameter, String alias);

    /**
     * Check is this registry contains parameter with this short alias
     *
     * @param alias alias of parameter to check
     * @return 'true' if this registry contains parameter with
     * this short alias or 'false' overwise
     */
    boolean hasAlias(String alias);

    /**
     * Remove already stored parameter by its name
     *
     * @param parameter name of parameter to remove
     * @return The removed instance of {@link Parameter}
     */
    Parameter remove(String parameter);

}
