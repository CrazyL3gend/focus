package ru.crazylegend.focus.util.actionbar;


import ru.crazylegend.focus.util.color.ColorUtils;

public class AdaptiveActionbar extends Actionbar {

    private AdaptiveActionbar(Actionbar actionbar) {
        super(actionbar);
    }

    public static AdaptiveActionbar adapt(Actionbar actionbar) {
        return new AdaptiveActionbar(actionbar);
    }

    public AdaptiveActionbar placeholder(String holder, Object to) {
        text = text.replaceAll(holder, ColorUtils.color(to.toString()));
        return this;
    }
}
