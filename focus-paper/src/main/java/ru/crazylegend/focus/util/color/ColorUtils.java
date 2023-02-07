package ru.crazylegend.focus.util.color;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class ColorUtils {

    public static final char ALTERNATIVE_CODE = '&';
    public static final String ALTERNATIVE_CODE_STRING = String.valueOf(ALTERNATIVE_CODE);

    public static final char DEFAULT_CODE = ChatColor.COLOR_CHAR;
    public static final String DEFAULT_CODE_STRING = String.valueOf(DEFAULT_CODE);

    private ColorUtils() {
        throw new UnsupportedOperationException();
    }

    public static String color(String input) {
        if (input == null) {
            return "";
        }
        if (!input.contains(ALTERNATIVE_CODE_STRING)) {
            return input;
        }
        return ChatColor.translateAlternateColorCodes(ALTERNATIVE_CODE, input);
    }

    public static List<String> color(String... inputs) {
        return Arrays.stream(inputs)
                .map(ColorUtils::color)
                .collect(Collectors.toList());
    }

    public static List<String> color(Collection<String> inputs) {
        return inputs.stream()
                .map(ColorUtils::color)
                .collect(Collectors.toList());
    }

    public static <R extends Collection<String>> R color(Collection<String> inputs, Collector<String, ?, R> collector) {
        return inputs.stream()
                .map(ColorUtils::color)
                .collect(collector);
    }

    //

    public static String uncolor(String input) {
        if (input == null) {
            return "";
        }
        if (!input.contains(DEFAULT_CODE_STRING)) {
            return input;
        }
        return ChatColor.stripColor(input);
    }

    public static List<String> uncolor(String... inputs) {
        return Arrays.stream(inputs)
                .map(ColorUtils::uncolor)
                .collect(Collectors.toList());
    }

    public static List<String> uncolor(Collection<String> inputs) {
        return inputs.stream()
                .map(ColorUtils::uncolor)
                .collect(Collectors.toList());
    }

    public static <R extends Collection<String>> R uncolor(Collection<String> inputs, Collector<String, ?, R> collector) {
        return inputs.stream()
                .map(ColorUtils::uncolor)
                .collect(collector);
    }

}
