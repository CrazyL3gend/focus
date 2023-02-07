package ru.crazylegend.focus.util.math.progress;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ProgressState {

    private double maximum, current;

    public ProgressState(double maximum, double current) {
        if (current > maximum) {
            throw new IllegalArgumentException("Current more than maximum!");
        }
        this.maximum = maximum;
        this.current = current;
    }

    public ProgressState(double maximum) {
        this(maximum, 0);
    }

    public double getCurrent() {
        return current;
    }

    public double getMaximum() {
        return maximum;
    }

    public ProgressState add(final double current) {
        this.current += current;
        if (current > maximum) {
            throw new IllegalArgumentException("Current more than maximum!");
        }
        return this;
    }

    public ProgressState subtract(final double current) {
        this.current -= current;
        if (current > maximum) {
            throw new IllegalArgumentException("Current more than maximum!");
        }
        return this;
    }

    public ProgressState multiply(final double current) {
        this.current *= current;
        if (current > maximum) {
            throw new IllegalArgumentException("Current more than maximum!");
        }
        return this;
    }

    public ProgressState share(final double current) {
        this.current /= current;
        if (current > maximum) {
            throw new IllegalArgumentException("Current more than maximum!");
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgressState that = (ProgressState) o;
        return new EqualsBuilder().append(maximum, that.maximum).append(current, that.current).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(maximum).append(current).toHashCode();
    }
}
