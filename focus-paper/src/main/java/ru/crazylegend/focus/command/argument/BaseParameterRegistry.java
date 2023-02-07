package ru.crazylegend.focus.command.argument;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.command.argument.suggestion.SuggestionProvider;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The internal maps-based {@link ParameterRegistry} implementation
 *
 * @since 1.12.0
 */
public final class BaseParameterRegistry implements ParameterRegistry {

    private final Map<String, Parameter> parameters;
    private final Map<String, Parameter> aliases;

    private boolean saveUnknownParameters;
    private String permissionMask;

    public BaseParameterRegistry() {
        this.parameters = new HashMap<>();
        this.aliases = new HashMap<>();

        this.saveUnknownParameters = true;
    }

    @Override
    public boolean isSavingUnknownParameters() {
        return saveUnknownParameters;
    }

    @Override
    public ParameterRegistry saveUnknownParameters(boolean value) {
        this.saveUnknownParameters = value;
        return this;
    }

    @Override
    public String getPermissionMask() {
        return permissionMask;
    }

    @Override
    public ParameterRegistry setPermissionMask(String permissionMask) {
        this.permissionMask = permissionMask;
        return this;
    }

    @Override
    public ParameterBuilder create() {
        return new BaseParameterBuilder(this, permissionMask);
    }

    @Override
    public Parameter add(Parameter parameter) {
        String name = parameter.getName();
        if (name.isEmpty())
            throw new IllegalArgumentException("'parameter' object must have non-empty name!");

        Parameter previous = parameters.put(name, parameter);
        if (previous != null && previous.hasShortAlias())
            aliases.remove(previous.getShortAlias());

        if (parameter.hasShortAlias())
            aliases.put(parameter.getShortAlias(), parameter);

        return previous;
    }

    @Override
    public Parameter get(String parameter) {
        if (parameter == null || parameter.isEmpty())
            throw new IllegalArgumentException("'parameter' cannot be null or empty!");

        return parameters.get(parameter);
    }

    @Override
    public Parameter getByAlias(String alias) {
        if (alias == null || alias.isEmpty())
            throw new IllegalArgumentException("'alias' cannot be null or empty!");

        return aliases.get(alias);
    }

    @Override
    public List<Parameter> getAllRegistered() {
        return Collections.unmodifiableList(new ArrayList<>(parameters.values()));
    }

    @Override
    public List<String> getRegisteredParameters() {
        return parameters.values()
                .stream()
                .map(Parameter::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRegisteredAliases() {
        return aliases.values()
                .stream()
                .map(Parameter::getShortAlias)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean has(String parameter) {
        if (parameter == null || parameter.isEmpty())
            throw new IllegalArgumentException("'parameter' cannot be null or empty!");

        return parameters.containsKey(parameter);
    }

    @Override
    public boolean has(String parameter, String alias) {
        return has(parameter) || hasAlias(alias);
    }

    @Override
    public boolean hasAlias(String alias) {
        if (alias == null || alias.isEmpty())
            throw new IllegalArgumentException("'alias' cannot be null or empty!");

        return aliases.containsKey(alias);
    }

    @Override
    public Parameter remove(String parameter) {
        if (parameter == null || parameter.isEmpty())
            throw new IllegalArgumentException("'parameter' cannot be null or empty!");

        return parameters.remove(parameter);
    }

    @Override
    public String toString() {
        return "ParameterRegistry{" +
                "saveUnknownParameters=" + saveUnknownParameters +
                ", parameters=" + parameters +
                ", aliases=" + aliases +
                '}';
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    static final class BaseParameter implements Parameter {
        private String name;
        private String shortAlias;
        private String permission;
        private SuggestionProvider suggestionProvider;
        private boolean valueRequired;

        @Override
        public String toString() {
            return "Parameter{" +
                    "name='" + name + '\'' +
                    ", shortAlias='" + shortAlias + '\'' +
                    ", permission='" + permission + '\'' +
                    ", suggestionProvider=" + suggestionProvider +
                    ", valueRequired=" + valueRequired +
                    '}';
        }
    }

    @Getter
    private static final class BaseParameterBuilder implements ParameterBuilder {
        private final String permissionMask;
        private final BaseParameterRegistry parameterRegistry;
        private final BaseParameter parameter;

        public BaseParameterBuilder(BaseParameterRegistry parameterRegistry, String permissionMask) {
            this.permissionMask = permissionMask;
            this.parameterRegistry = parameterRegistry;
            this.parameter = new BaseParameter();
        }

        @Override
        public @NotNull ParameterRegistry addToRegistry() {
            if (parameterRegistry == null)
                throw new UnsupportedOperationException("parameter registry was not specified for this builder");

            parameterRegistry.add(parameter);
            return parameterRegistry;
        }

        @Override
        public ParameterBuilder name(@NotNull String name) {
            parameter.name = name;
            if (permissionMask != null && !permissionMask.isEmpty() && !parameter.hasPermission())
                parameter.permission = String.format(permissionMask, name);
            return this;
        }

        @Override
        public ParameterBuilder shortAlias(String shortAlias) {
            parameter.shortAlias = shortAlias;
            return this;
        }

        @Override
        public ParameterBuilder permission(String permission) {
            parameter.permission = permission;
            return this;
        }

        @Override
        public ParameterBuilder suggestionProvider(SuggestionProvider suggestionProvider) {
            parameter.suggestionProvider = suggestionProvider;
            return this;
        }

        @Override
        public ParameterBuilder requiresValue(boolean valueRequired) {
            parameter.valueRequired = valueRequired;
            return this;
        }

        @Override
        public String toString() {
            return "ParameterBuilder{" +
                    "parameter=" + parameter +
                    ", parameterRegistry=" + parameterRegistry +
                    '}';
        }
    }

}
