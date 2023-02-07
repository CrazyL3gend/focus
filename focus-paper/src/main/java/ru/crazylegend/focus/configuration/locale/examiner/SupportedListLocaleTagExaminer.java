package ru.crazylegend.focus.configuration.locale.examiner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiPredicate;

public final class SupportedListLocaleTagExaminer implements LocaleTagExaminer {

    private final @NotNull Set<String> supportedLocaleTags;
    private final boolean isCaseSensitive;

    public SupportedListLocaleTagExaminer(boolean isCaseSensitive, @NotNull String... supportedLocaleTags) {
        Validate.notNull(supportedLocaleTags, "supportedLocaleTags");
        this.supportedLocaleTags = new LinkedHashSet<>(Arrays.asList(supportedLocaleTags));
        this.isCaseSensitive = isCaseSensitive;
    }

    public SupportedListLocaleTagExaminer(boolean isCaseSensitive, @NotNull Collection<String> supportedLocaleTags) {
        Validate.notNull(supportedLocaleTags, "supportedLocaleTags");
        this.supportedLocaleTags = new LinkedHashSet<>(supportedLocaleTags);
        this.isCaseSensitive = isCaseSensitive;
    }

    @Override
    public boolean test(@Nullable String localeTag) {
        if (localeTag == null || supportedLocaleTags.isEmpty())
            return false;

        return contains(localeTag, isCaseSensitive ? String::equals : String::equalsIgnoreCase);
    }

    private boolean contains(@NotNull String key, @NotNull BiPredicate<String, String> filter) {
        for (String localeTag : supportedLocaleTags)
            if (filter.test(key, localeTag))
                return true;

        return false;
    }

}
