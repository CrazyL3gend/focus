package ru.crazylegend.focus.util.math.probable;

import java.util.concurrent.ThreadLocalRandom;

public class ProbableGeneration {

    public static final ProbableGeneration HUNDRED = new ProbableGeneration(0, 100);
    public static final ProbableGeneration TEN = new ProbableGeneration(0, 10);

    private int min, max;

    public ProbableGeneration(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getRandomly() {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
