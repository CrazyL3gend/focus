package ru.crazylegend.focus.configuration;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.configuration.locale.examiner.LocaleTagExaminer;
import ru.crazylegend.focus.configuration.locale.resolver.LocaleTagResolver;
import ru.crazylegend.focus.util.tool.Validate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class LocalizableMessages extends Messages {

    public static final String TAG_PLACEHOLDER = "{tag}";
    public static final String DEFAULT_LOCALE_TAG = "en";
    public static final String LOCALE_RESOURCE_PATH_FORMAT = "messages_{tag}.yml";
    public static final String LOCALE_OUTPUT_FILE_PATH_FORMAT = "messages_{tag}.yml";

    protected @NotNull String defaultLocaleTag = DEFAULT_LOCALE_TAG;
    protected @NotNull String localeResourcePathFormat = LOCALE_RESOURCE_PATH_FORMAT;
    protected @NotNull String localeOutputFilePathFormat = LOCALE_OUTPUT_FILE_PATH_FORMAT;
    protected @NotNull LocaleTagResolver currentLocaleResolver = () -> defaultLocaleTag;
    protected @NotNull LocaleTagExaminer currentLocaleExaminer = tag -> true;

    public LocalizableMessages(@NotNull JavaPlugin plugin) {
        super(plugin, "");
    }

    public LocalizableMessages(@NotNull JavaPlugin plugin, @NotNull Path dataFolderPath) {
        super(plugin, "", dataFolderPath, null);
    }

    /**
     * Get the actual and always valid locale tag that will be used for the localization selecting on next refresh.<br>
     * It may be a valid custom user's locale tag or fallback default locale tag.
     *
     * @return The actual locale tag.
     */
    public @NotNull String getActualLocaleTag() {
        String localeTag = currentLocaleResolver.resolve();
        return localeTag != null && currentLocaleExaminer.test(localeTag) ? localeTag : defaultLocaleTag;
    }

    @Override
    protected @NotNull String getResourcePath() {
        String localeTag = getActualLocaleTag();
        return localeResourcePathFormat.replace(TAG_PLACEHOLDER, localeTag);
    }

    @Override
    protected @NotNull String getOutputFilePath() {
        String localeTag = getActualLocaleTag();
        return localeOutputFilePathFormat.replace(TAG_PLACEHOLDER, localeTag);
    }

    @Override
    protected void createOutputFile(@NotNull Path filePath, boolean verbose) throws IOException {
        super.resource = plugin.getResource(getResourcePath());
        super.createOutputFile(filePath, verbose);
    }

    /**
     * Get the default locale tag that will be used when a custom locale does not found.
     *
     * @return The default locale tag.
     */
    public @NotNull String getDefaultLocaleTag() {
        return defaultLocaleTag;
    }

    /**
     * Set the default locale tag.<br>
     * See an annotated method for more information.
     *
     * @param defaultLocaleTag The default locale tag.
     * @return The current instance of {@link LocalizableMessages} to continue the method chain.
     * @see #getDefaultLocaleTag()
     */
    public @NotNull LocalizableMessages withDefaultLocaleTag(@NotNull String defaultLocaleTag) {
        Validate.notNull(defaultLocaleTag, "defaultLocaleTag");
        this.defaultLocaleTag = defaultLocaleTag;
        return this;
    }

    /**
     * Get the path format to a locale resource inside a plugin JAR.<br>
     * If this resource does not found then a default locale will be used instead.<br>
     * You can use the <b>{tag}</b> placeholder here for an actually used locale tag.
     *
     * @return The locale resource path format.
     */
    public @NotNull String getLocaleResourcePathFormat() {
        return localeResourcePathFormat;
    }

    /**
     * Set the locale resource path format.<br>
     * See an annotated method for more information.
     *
     * @param localeResourcePathFormat The locale resource path format.
     * @return The current instance of {@link LocalizableMessages} to continue the method chain.
     * @see #getLocaleResourcePathFormat()
     */
    public @NotNull LocalizableMessages withLocaleResourcePathFormat(@NotNull String localeResourcePathFormat) {
        Validate.notNull(localeResourcePathFormat, "localeResourcePathFormat");
        this.localeResourcePathFormat = localeResourcePathFormat;
        return this;
    }

    /**
     * Get the path format to an output localization file in the data folder.<br>
     * You can use the <b>{tag}</b> placeholder here for an actually used locale tag.
     *
     * @return The locale output file path format.
     */
    public @NotNull String getLocaleOutputFilePathFormat() {
        return localeOutputFilePathFormat;
    }

    /**
     * Set the locale output file path format.<br>
     * See an annotated method for more information.
     *
     * @param localeOutputFilePathFormat The locale output file path format.
     * @return The current instance of {@link LocalizableMessages} to continue the method chain.
     * @see #getLocaleOutputFilePathFormat()
     */
    public @NotNull LocalizableMessages withLocaleOutputFilePathFormat(@NotNull String localeOutputFilePathFormat) {
        Validate.notNull(localeOutputFilePathFormat, "localeOutputFilePathFormat");
        this.localeOutputFilePathFormat = localeOutputFilePathFormat.replace('/', File.separatorChar);
        return this;
    }

    /**
     * Get the resolver (provider) of the current locale tag that your user want to use.<br>
     * The default resolver will return the default locale tag.
     *
     * @return The current locale tag resolver.
     */
    public @NotNull LocaleTagResolver getCurrentLocaleResolver() {
        return currentLocaleResolver;
    }

    /**
     * Set the current locale tag resolver (provider).<br>
     * See an annotated method for more information.
     *
     * @param currentLocaleResolver The locale tag resolver.
     * @return The current instance of {@link LocalizableMessages} to continue the method chain.
     * @see #getCurrentLocaleResolver()
     */
    public @NotNull LocalizableMessages withCurrentLocaleResolver(@NotNull LocaleTagResolver currentLocaleResolver) {
        Validate.notNull(currentLocaleResolver, "currentLocaleResolver");
        this.currentLocaleResolver = currentLocaleResolver;
        return this;
    }

    /**
     * Get the examiner of the current locale tag that your user want to use.<br>
     * You can use this to filter a locale tags with only supported.<br>
     * The default examiner will accept all tags.
     *
     * @return The current locale tag examiner.
     */
    public @NotNull LocaleTagExaminer getCurrentLocaleExaminer() {
        return currentLocaleExaminer;
    }

    /**
     * Set the current locale tag examiner.<br>
     * See an annotated method for more information.
     *
     * @param currentLocaleExaminer The locale tag examiner.
     * @return The current instance of {@link LocalizableMessages} to continue the method chain.
     * @see #getCurrentLocaleExaminer()
     */
    public @NotNull LocalizableMessages withCurrentLocaleExaminer(@NotNull LocaleTagExaminer currentLocaleExaminer) {
        Validate.notNull(currentLocaleExaminer, "currentLocaleExaminer");
        this.currentLocaleExaminer = currentLocaleExaminer;
        return this;
    }

}
