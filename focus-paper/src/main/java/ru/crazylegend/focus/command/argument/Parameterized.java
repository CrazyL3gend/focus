package ru.crazylegend.focus.command.argument;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents an instance which can be parameterized using full or short parameters
 *
 * @since 1.12.0
 */
public interface Parameterized {

    String PARAMETER_FULL_PREFIX = "--";
    String PARAMETER_ALIAS_PREFIX = "-";
    String PARAMETER_VALUE_DELIMITER = "=";
    String MULTIWORD_VALUE_LIMITER = "\"";

    /**
     * Parse a parameter & value pair from this input string
     *
     * @param input input string to use as data source
     * @return A new array with two strings: name [0] and value [1]
     */
    static String[] parseValuedPair(String input) {
        int firstIndexOf = input.indexOf('=');

        // '=' is not found
        if (firstIndexOf == -1)
            return new String[]{input, ""};

        // '=' is first symbol
        if (firstIndexOf == 0) {
            String name = "";
            String value = input.length() > 1 ? input.substring(1) : "";
            return new String[]{name, value};
        }

        // '=' is last symbol
        if (firstIndexOf == input.length() - 1) {
            String name = input.substring(0, input.length() - 1);
            String value = "";
            return new String[]{name, value};
        }

        // the valid case
        String name = input.substring(0, firstIndexOf);
        String value = input.substring(firstIndexOf + 1);
        return new String[]{name, value};
    }

    /**
     * Check is any parameters were found in the arguments
     *
     * @return 'true' if any parameters found or 'false' overwise
     */
    boolean anyParametersFound();

    /**
     * Get immutable copy of the parameters map found in the command arguments
     *
     * @return A map copy of found parameters
     */
    Map<String, ParameterValue> getFoundParameters();

    /**
     * Get immutable copy of the parameter aliases map found in the command arguments
     *
     * @return A map copy of found parameter aliases
     */
    Map<String, ParameterValue> getFoundParametersAliases();

    /**
     * Check is the command arguments has this parameter or its short alias
     *
     * @param parameter parameter instance to check
     * @return 'true' if this parameter was specified in the arguments or 'false' overwise
     */
    default boolean hasParameter(Parameter parameter) {
        return hasParameter(parameter.getName(), parameter.getShortAlias());
    }

    /**
     * Check is the command arguments has this parameter
     *
     * @param parameter parameter to check
     * @return 'true' if this parameter was specified in the arguments or 'false' overwise
     */
    boolean hasParameter(String parameter);

    /**
     * Check is the command arguments has this parameter
     *
     * @param parameter parameter to check
     * @param alias     parameter alias to check
     * @return 'true' if this parameter was specified in the arguments or 'false' overwise
     */
    boolean hasParameter(String parameter, String alias);

    /**
     * Check is the command arguments has this parameter using its short alias
     *
     * @param alias parameter alias to check
     * @return 'true' if this parameter was specified in the arguments or 'false' overwise
     */
    boolean hasParameterAlias(String alias);

    /**
     * Get value specified for this parameter
     *
     * @param parameter parameter instance to get value
     * @return The parameter value as instance of {@link ParameterValue}
     */
    @NotNull
    default ParameterValue getParameterValue(Parameter parameter) {
        return getParameterValue(parameter.getName(), parameter.getShortAlias());
    }

    /**
     * Get value specified for this parameter
     *
     * @param parameter parameter name to get value
     * @return The parameter value as instance of {@link ParameterValue}
     */
    @NotNull
    ParameterValue getParameterValue(String parameter);

    /**
     * Get value specified for this parameter
     *
     * @param parameter parameter name to get value
     * @param alias     parameter alias to get value
     * @return The parameter value as instance of {@link ParameterValue}
     */
    @NotNull
    ParameterValue getParameterValue(String parameter, String alias);

    /**
     * Get value specified for parameter using its short alias
     *
     * @param alias parameter alias to get value
     * @return The parameter value as instance of {@link ParameterValue}
     */
    @NotNull
    ParameterValue getParameterValueByAlias(String alias);

}
