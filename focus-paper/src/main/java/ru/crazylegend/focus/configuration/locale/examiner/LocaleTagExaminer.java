package ru.crazylegend.focus.configuration.locale.examiner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@FunctionalInterface
public interface LocaleTagExaminer {

    static @NotNull LocaleTagExaminer fromList(boolean isCaseSensitive, @NotNull String... supportedLocaleTags) {
        return new SupportedListLocaleTagExaminer(isCaseSensitive, supportedLocaleTags);
    }

    static @NotNull LocaleTagExaminer fromList(boolean isCaseSensitive, @NotNull Collection<String> supportedLocaleTags) {
        return new SupportedListLocaleTagExaminer(isCaseSensitive, supportedLocaleTags);
    }

    boolean test(@Nullable String localeTag);

}
