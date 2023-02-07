package ru.crazylegend.focus.util.math.probable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public abstract class AbstractProbable implements Probable {

    protected Probability chance;

    protected AbstractProbable(Probability chance) {
        this.chance = chance;
    }

    @Override
    public Probability getChance() {
        return chance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractProbable that = (AbstractProbable) o;
        return new EqualsBuilder().append(chance, that.chance).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(chance).toHashCode();
    }
}
