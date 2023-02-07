package ru.crazylegend.focus.util.math.probable;

public class RandomlyProbableSelector<P extends Probable> implements ProbableSelector<P> {

    private Iterable<? extends P> iterable;

    RandomlyProbableSelector(Iterable<? extends P> iterable) {
        this.iterable = iterable;
    }

    @Override
    public P select(Probability common) {
        final Probability random = Probability.randomly(common.getChance());
        Probability cumulative = Probability.zero();
        for (final P probable : iterable) {
            cumulative = cumulative.add(probable.getChance());
            if (cumulative.compareTo(random) >= 0) {
                return probable;
            }
        }
        throw new IllegalArgumentException("Common probability incorrect");
    }

}
