package ru.crazylegend.focus.util.math.probable;

import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class Probability implements Comparable<Probability> {

    private final int chance;

    private Probability(final int chance) {
        if (chance < 0) {
            throw new IllegalArgumentException("Chance cannot be less than 0!");
        }
        this.chance = chance;
    }

    public static Comparator<Probability> comparator() {
        return Probability::compareTo;
    }

    public static Probability constant(int chance) {
        return new Probability(chance);
    }

    public static Probability zero() {
        return constant(0);
    }

    public static Probability randomly(int min, int max) {
        return constant(ThreadLocalRandom.current().nextInt(min, max));
    }

    public static Probability randomly(int max) {
        return randomly(0, max);
    }

    public static Probability randomly() {
        return randomly(0, 100);
    }

    public static Probability total(Iterable<? extends Probability> iterable) {
        Probability probability = Probability.zero();
        for (final Probability got : iterable) {
            probability = probability.add(got);
        }
        return probability;
    }

    public static Probability total(Probability... probabilities) {
        return total(Lists.newArrayList(probabilities));
    }

    //

    public int getChance() {
        return chance;
    }

    public Probability add(int chance) {
        return Probability.constant(this.chance + chance);
    }

    public Probability add(Probability probability) {
        return add(probability.getChance());
    }

    public Probability subtract(int chance) {
        return Probability.constant(this.chance - chance);
    }

    public Probability subtract(Probability probability) {
        return subtract(probability.getChance());
    }

    public Probability multiply(int chance) {
        return Probability.constant(this.chance * chance);
    }

    public Probability multiply(Probability probability) {
        return multiply(probability.getChance());
    }

    public Probability share(int chance) {
        return Probability.constant(this.chance / chance);
    }

    public Probability share(Probability probability) {
        return share(probability.getChance());
    }

    @Override
    public int compareTo(Probability probability) {
        return chance - probability.chance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Probability that = (Probability) o;
        return new EqualsBuilder().append(chance, that.chance).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(chance).toHashCode();
    }

}
