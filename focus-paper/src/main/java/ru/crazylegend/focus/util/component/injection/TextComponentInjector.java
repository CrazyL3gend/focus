package ru.crazylegend.focus.util.component.injection;


import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import ru.crazylegend.focus.util.component.replacement.TextComponentReplacement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TextComponentInjector {

    public static TextComponent inject(String message, TextComponentReplacement replacement) {
        return inject(message, replacement.getPlaceholder(), replacement.getValue());
    }

    public static TextComponent inject(String message, String placeholder, TextComponent value) {
        if (message == null || message.isEmpty()) return new TextComponent("");

        BaseComponent[] initialArray = TextComponent.fromLegacyText(message);
        if (initialArray.length == 0) return new TextComponent(message);

        BaseComponent[] injected = inject(initialArray, placeholder, value);
        return new TextComponent(injected);
    }

    public static TextComponent inject(String message, TextComponentReplacement... replacements) {
        BaseComponent[] injected = inject(TextComponent.fromLegacyText(message), replacements);
        return new TextComponent(injected);
    }

    public static BaseComponent[] inject(BaseComponent[] message, TextComponentReplacement replacement) {
        return inject(message, replacement.getPlaceholder(), replacement.getValue());
    }

    public static BaseComponent[] inject(BaseComponent[] message, String placeholder, TextComponent value) {
        if (message == null || message.length == 0) return TextComponent.fromLegacyText("");

        return Arrays.stream(message)
                .map(component -> {
                    String text = component.toPlainText();
                    if (!text.contains(placeholder))
                        return Collections.singletonList(component);

                    String before = null;
                    String after = null;

                    int indexOf = text.indexOf(placeholder);
                    if (!text.startsWith(placeholder))
                        before = text.substring(0, indexOf);

                    if (!text.endsWith(placeholder))
                        after = text.substring(indexOf + placeholder.length(), text.length());

                    List<BaseComponent> components = new ArrayList<>();

                    if (before != null) {
                        TextComponent beforeComponent = new TextComponent(before);
                        beforeComponent.copyFormatting(component);
                        components.add(beforeComponent);
                    }

                    components.add(value);

                    if (after != null) {
                        TextComponent afterComponent = new TextComponent(after);
                        afterComponent.copyFormatting(component);
                        components.add(afterComponent);
                    }

                    return components;
                })
                .flatMap(List::stream)
                .collect(Collectors.toList())
                .toArray(new BaseComponent[0]);
    }

    public static BaseComponent[] inject(BaseComponent[] message, TextComponentReplacement... replacements) {
        if (message == null || message.length == 0) return TextComponent.fromLegacyText("");

        for (TextComponentReplacement replacement : replacements)
            message = inject(message, replacement);

        return message;
    }

}
