package ru.crazylegend.focus.util.math.probable;

public final class ProbableUtil {

    private ProbableUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean isProbable(ProbableGeneration generation, int value) {
        return generation.getRandomly() <= value;
    }

    public static boolean isProbable(int value) {
        return isProbable(ProbableGeneration.HUNDRED, value);
    }

}
