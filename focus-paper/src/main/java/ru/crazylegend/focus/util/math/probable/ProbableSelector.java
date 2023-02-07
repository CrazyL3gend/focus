package ru.crazylegend.focus.util.math.probable;

public interface ProbableSelector<P extends Probable> {

    static <P extends Probable> ProbableSelector<P> randomly(Iterable<? extends P> iterable) {
        return new RandomlyProbableSelector<>(iterable);
    }

    P select(Probability common);

}
