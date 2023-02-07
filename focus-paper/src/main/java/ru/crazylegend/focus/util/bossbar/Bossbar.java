package ru.crazylegend.focus.util.bossbar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import ru.crazylegend.focus.api.Creatable;
import ru.crazylegend.focus.util.color.ColorUtils;


public class Bossbar {

    private double progress = 1;
    private String text;
    private BarStyle style = BarStyle.SOLID;
    private BarColor color = BarColor.WHITE;
    private BarFlag[] flags = new BarFlag[0];

    public Bossbar(String text, double progress, BarStyle style, BarColor color, BarFlag... flags) {
        this.text = ColorUtils.color(text);
        this.progress = progress;
        this.style = style;
        this.color = color;
        this.flags = flags;
    }

    public Bossbar(String text, double progress) {
        this.text = ColorUtils.color(text);
        this.progress = progress;
    }

    public Bossbar(String text) {
        this.text = ColorUtils.color(text);
    }

    public double getProgress() {
        return progress;
    }

    public String getText() {
        return text;
    }

    public BarColor getColor() {
        return color;
    }

    public BarFlag[] getFlags() {
        return flags;
    }

    public BarStyle getStyle() {
        return style;
    }

    public BossbarSession createSession() {
        return BossbarSession.session(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bossbar bossBar = (Bossbar) o;
        return new EqualsBuilder()
                .append(progress, bossBar.progress)
                .append(text, bossBar.text)
                .append(style, bossBar.style)
                .append(color, bossBar.color)
                .append(flags, bossBar.flags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(progress)
                .append(text)
                .append(style)
                .append(color)
                .append(flags)
                .toHashCode();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder implements Creatable<Bossbar> {

        private double progress = 1;
        private String text = "";
        private BarStyle style = BarStyle.SOLID;
        private BarColor color = BarColor.WHITE;
        private BarFlag[] flags = new BarFlag[0];

        private Builder() {
        }

        public Builder setProgress(double progress) {
            this.progress = progress;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setStyle(BarStyle style) {
            this.style = style;
            return this;
        }

        public Builder setColor(BarColor color) {
            this.color = color;
            return this;
        }

        public Builder setFlags(BarFlag... flags) {
            this.flags = flags;
            return this;
        }

        @Override
        public Bossbar create() {
            return new Bossbar(text, progress, style, color, flags);
        }
    }
}
