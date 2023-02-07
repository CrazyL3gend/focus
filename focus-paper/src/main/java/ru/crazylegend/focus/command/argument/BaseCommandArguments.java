package ru.crazylegend.focus.command.argument;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.command.exception.ParameterValueRequiredException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The internal list-based {@link CommandArguments} implementation
 */
public final class BaseCommandArguments implements CommandArguments {

    private final List<String> arguments;
    private final List<String> rawArguments;
    private final DispatchPath dispatchPath;
    private final ParameterRegistry parameterRegistry;

    private final Map<String, ParameterValue> foundParameters;
    private final Map<String, ParameterValue> foundAliases;

    public BaseCommandArguments(CommandSender sender, String[] arrayArguments, ParameterRegistry parameterRegistry)
            throws ParameterValueRequiredException {
        this.arguments = new ArrayList<>();
        this.rawArguments = new ArrayList<>();
        this.dispatchPath = new BaseDispatchPath();
        this.parameterRegistry = parameterRegistry;

        this.foundParameters = new HashMap<>();
        this.foundAliases = new HashMap<>();

        parseRawArgs(sender, arrayArguments);
    }

    private void parseRawArgs(CommandSender sender, String[] arrayArguments) throws ParameterValueRequiredException {
        int argsAmount = arrayArguments.length;
        if (argsAmount == 0) return;

        for (int i = 0; i < argsAmount; i++) {
            String arrayArgument = arrayArguments[i];
            rawArguments.add(arrayArgument);
            int argLength = arrayArgument.length();

            String parameterArgument;
            boolean isFullParameter = true;

            if (arrayArgument.startsWith(PARAMETER_FULL_PREFIX) && argLength > 2) {
                parameterArgument = arrayArgument.substring(2);
            } else if (arrayArgument.startsWith(PARAMETER_ALIAS_PREFIX) && argLength > 1) {
                parameterArgument = arrayArgument.substring(1);
                isFullParameter = false;
            } else {
                arguments.add(arrayArgument);
                continue;
            }

            String[] parts = parameterArgument.split(PARAMETER_VALUE_DELIMITER);
            if (parts.length == 0) return;

            String param = parts[0];
            Parameter parameter = isFullParameter
                    ? parameterRegistry.get(param)
                    : parameterRegistry.getByAlias(param);

            if (parameter == null && !parameterRegistry.isSavingUnknownParameters())
                continue;

            if (parameter != null && !parameter.canUse(sender))
                continue;

            StringBuilder value = new StringBuilder();
            if (parts.length > 1) {
                value = new StringBuilder(parts[1]);

                // multi-word value parsing
                if (value.toString().startsWith(MULTIWORD_VALUE_LIMITER) && value.length() > 1) {
                    // 'one-word-ends-with-limiter'
                    if (value.toString().endsWith(MULTIWORD_VALUE_LIMITER)) {
                        if (value.length() != 2)
                            value = new StringBuilder(value.substring(1, value.length() - 1));
                    }

                    // 'multi-words value ends with limiter'
                    if (i < argsAmount - 1) {
                        value = new StringBuilder(value.substring(1));
                        for (int j = i + 1; j < argsAmount; j++) {
                            String nextArg = arrayArguments[j];
                            value.append(" ").append(nextArg);
                            i++;

                            if (nextArg.endsWith(MULTIWORD_VALUE_LIMITER)) {
                                if (nextArg.length() > 1 && nextArg.charAt(nextArg.length() - 2) != '\\') {
                                    value = new StringBuilder(value.substring(0, value.length() - 1));
                                    break;
                                }
                            }
                        }
                    }
                }

                value = new StringBuilder(value.toString().replace("\\" + MULTIWORD_VALUE_LIMITER, MULTIWORD_VALUE_LIMITER));
            }

            boolean valueRequired = parameter != null && parameter.isValueRequired();
            if (valueRequired && (value.length() == 0))
                throw new ParameterValueRequiredException(param);

            if (isFullParameter)
                foundParameters.put(param, new BaseParameterValue(value.toString()));
            else
                foundAliases.put(param, new BaseParameterValue(value.toString()));
        }
    }

    /*******************
     *    ARGUMENTS    *
     ******************/

    @Override
    public List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public List<String> getRaw() {
        return Collections.unmodifiableList(rawArguments);
    }

    @Override
    public @NotNull DispatchPath getDispatchPath() {
        return dispatchPath;
    }

    @Override
    public Stream<String> stream() {
        return arguments.stream();
    }

    @Override
    public boolean removeIf(Predicate<String> filter) {
        return arguments.removeIf(filter);
    }

    @Override
    public double getAsDouble(int index) {
        if (index >= size())
            return -1D;

        try {
            return Double.parseDouble(get(index));
        } catch (Exception ignored) {
            return -1D;
        }
    }

    @Override
    public float getAsFloat(int index) {
        if (index >= size())
            return -1F;

        try {
            return Float.parseFloat(get(index));
        } catch (Exception ignored) {
            return -1F;
        }
    }

    @Override
    public int getAsInteger(int index) {
        if (index >= size())
            return -1;

        try {
            return Integer.parseInt(get(index));
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public long getAsLong(int index) {
        if (index >= size())
            return -1L;

        try {
            return Long.parseLong(get(index));
        } catch (Exception ignored) {
            return -1L;
        }
    }

    @Override
    public String remove(int index) {
        return hasArgument(index) ? arguments.remove(index) : null;
    }

    @Override
    public boolean remove(Object element) {
        return arguments.remove(element);
    }

    @Override
    public double removeDouble(int index) {
        if (index >= size())
            return -1D;

        try {
            String removed = remove(index);
            return removed != null ? Double.parseDouble(removed) : -1D;
        } catch (Exception ignored) {
            return -1D;
        }
    }

    @Override
    public float removeFloat(int index) {
        if (index >= size())
            return -1F;

        try {
            String removed = remove(index);
            return removed != null ? Float.parseFloat(removed) : -1F;
        } catch (Exception ignored) {
            return -1F;
        }
    }

    @Override
    public int removeInteger(int index) {
        if (index >= size())
            return -1;

        try {
            String removed = remove(index);
            return removed != null ? Integer.parseInt(removed) : -1;
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public long removeLong(int index) {
        if (index >= size())
            return -1L;

        try {
            String removed = remove(index);
            return removed != null ? Long.parseLong(removed) : -1L;
        } catch (Exception ignored) {
            return -1L;
        }
    }

    /********************
     *    PARAMETERS    *
     *******************/

    @Override
    public boolean anyParametersFound() {
        return !foundParameters.isEmpty() || !foundAliases.isEmpty();
    }

    @Override
    public Map<String, ParameterValue> getFoundParameters() {
        return Collections.unmodifiableMap(foundParameters);
    }

    @Override
    public Map<String, ParameterValue> getFoundParametersAliases() {
        return Collections.unmodifiableMap(foundAliases);
    }

    @Override
    public boolean hasParameter(String parameter) {
        return foundParameters.containsKey(parameter);
    }

    @Override
    public boolean hasParameter(String parameter, String alias) {
        return hasParameter(parameter) || (alias != null && hasParameterAlias(alias));
    }

    @Override
    public boolean hasParameterAlias(String alias) {
        return foundAliases.containsKey(alias);
    }

    @Override
    public @NotNull ParameterValue getParameterValue(String parameter) {
        ParameterValue value = foundParameters.get(parameter);
        return value != null ? value : BaseParameterValue.EMPTY;
    }

    @Override
    public @NotNull ParameterValue getParameterValue(String parameter, String alias) {
        ParameterValue value = getParameterValue(parameter);
        if (!value.hasValue() && alias != null)
            return getParameterValueByAlias(alias);

        return BaseParameterValue.EMPTY;
    }

    @Override
    public @NotNull ParameterValue getParameterValueByAlias(String alias) {
        ParameterValue value = foundAliases.get(alias);
        return value != null ? value : BaseParameterValue.EMPTY;
    }

    @Override
    public String toString() {
        return "CommandArguments{" +
                "arguments=" + arguments +
                ", parameterRegistry=" + parameterRegistry +
                ", foundParameters=" + foundParameters +
                ", foundAliases=" + foundAliases +
                '}';
    }

}
