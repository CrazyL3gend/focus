package ru.crazylegend.focus.configuration.locale.resolver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.configuration.AbstractConfiguration;

@FunctionalInterface
public interface LocaleTagResolver {

    static @NotNull LocaleTagResolver fromConfig(@NotNull AbstractConfiguration config, @NotNull String localeTagKey) {
        return new ConfigProvidedLocaleTagResolver(config, localeTagKey);
    }

    @Nullable String resolve();

}
