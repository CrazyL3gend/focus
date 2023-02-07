package ru.crazylegend.focus.configuration.locale.resolver;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.configuration.AbstractConfiguration;
import ru.crazylegend.focus.util.tool.Validate;

public final class ConfigProvidedLocaleTagResolver implements LocaleTagResolver {

    private final @NotNull AbstractConfiguration config;
    private final @NotNull String localeTagKey;

    public ConfigProvidedLocaleTagResolver(@NotNull AbstractConfiguration config, @NotNull String localeTagKey) {
        Validate.notNull(config, "config");
        Validate.notNull(localeTagKey, "localeTagKey");
        this.config = config;
        this.localeTagKey = localeTagKey;
    }

    @Override
    public @Nullable String resolve() {
        return config.getString(localeTagKey);
    }

}