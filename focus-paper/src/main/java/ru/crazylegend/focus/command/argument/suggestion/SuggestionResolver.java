package ru.crazylegend.focus.command.argument.suggestion;

import org.bukkit.command.CommandSender;
import ru.crazylegend.focus.command.argument.CommandArguments;
import ru.crazylegend.focus.command.argument.Parameter;
import ru.crazylegend.focus.command.argument.ParameterRegistry;
import ru.crazylegend.focus.command.argument.Parameterized;

import java.util.List;
import java.util.Objects;

/**
 * The internal utility class to resolve the parameter suggestions
 *
 * @since 1.12.0
 */
public class SuggestionResolver {

    @SuppressWarnings("ConstantConditions")
    public static void resolveSuggestions(
            List<String> suggestions,
            ParameterRegistry parameterRegistry,
            CommandSender sender,
            CommandArguments args,
            boolean duplicateSuggestions
    ) {
        String arg = args.getRaw().get(args.getRaw().size() - 1);

        if (arg.startsWith(Parameterized.PARAMETER_FULL_PREFIX) && arg.length() > 2) {
            String[] parts = Parameterized.parseValuedPair(arg.substring(2));
            String name = parts[0];
            String value = parts[1];
            if (!name.isEmpty()) {
                Parameter parameter = parameterRegistry.get(name);
                if (isValidParameter(parameter, false) && parameter.canUse(sender)) {
                    List<String> parameterSuggestions = parameter.getSuggestionProvider().provideSuggestions(sender, value);
                    if (parameterSuggestions == null)
                        return;

                    parameterSuggestions.stream()
                            .map(s -> parameterAsSuggestion(parameter, s))
                            .filter(s -> s.toLowerCase().startsWith(arg))
                            .forEach(suggestions::add);
                    return;
                }
            }
        }

        if (arg.startsWith(Parameterized.PARAMETER_ALIAS_PREFIX) && arg.length() > 1) {
            String[] parts = Parameterized.parseValuedPair(arg.substring(1));
            String name = parts[0];
            String value = parts[1];
            if (!name.isEmpty()) {
                Parameter parameter = parameterRegistry.getByAlias(name);
                if (isValidParameter(parameter, true) && parameter.canUse(sender)) {
                    List<String> parameterSuggestions = parameter.getSuggestionProvider().provideSuggestions(sender, value);
                    if (parameterSuggestions == null)
                        return;

                    parameterSuggestions.stream()
                            .map(s -> parameterAliasAsSuggestion(parameter, s))
                            .filter(s -> s.toLowerCase().startsWith(arg))
                            .forEach(suggestions::add);
                    return;
                }
            }
        }

        parameterRegistry.getAllRegistered()
                .stream()
                .filter(p -> duplicateSuggestions || !args.hasParameter(p))
                .filter(p -> p.canUse(sender))
                .map(SuggestionResolver::parameterAsSuggestion)
                .filter(p -> p.toLowerCase().startsWith(arg))
                .forEach(suggestions::add);

        parameterRegistry.getAllRegistered()
                .stream()
                .filter(p -> duplicateSuggestions || !args.hasParameter(p))
                .filter(p -> p.canUse(sender))
                .map(SuggestionResolver::parameterAliasAsSuggestion)
                .filter(Objects::nonNull)
                .filter(p -> p.toLowerCase().startsWith(arg))
                .forEach(suggestions::add);
    }

    private static boolean isValidParameter(Parameter parameter, boolean useAlias) {
        if (parameter == null)
            return false;

        if (useAlias && !parameter.hasShortAlias())
            return false;

        return parameter.isValueRequired() && parameter.hasSuggestionProvider();
    }

    private static String parameterAsSuggestion(Parameter parameter) {
        String suggestion = Parameterized.PARAMETER_FULL_PREFIX + parameter.getName();
        return parameter.isValueRequired() ? suggestion + "=" : suggestion;
    }

    private static String parameterAsSuggestion(Parameter parameter, String value) {
        String suggestion = Parameterized.PARAMETER_FULL_PREFIX + parameter.getName();
        return suggestion + "=" + value;
    }

    private static String parameterAliasAsSuggestion(Parameter parameter) {
        if (!parameter.hasShortAlias())
            return null;

        String suggestion = Parameterized.PARAMETER_ALIAS_PREFIX + parameter.getShortAlias();
        return parameter.isValueRequired() ? suggestion + "=" : suggestion;
    }

    private static String parameterAliasAsSuggestion(Parameter parameter, String value) {
        if (!parameter.hasShortAlias())
            return null;

        String suggestion = Parameterized.PARAMETER_ALIAS_PREFIX + parameter.getShortAlias();
        return suggestion + "=" + value;
    }

}
