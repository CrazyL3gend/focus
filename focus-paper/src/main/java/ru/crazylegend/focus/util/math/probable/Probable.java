package ru.crazylegend.focus.util.math.probable;

public interface Probable extends Comparable<Probable> {

    default int compareTo(Probable probable) {
        return getChance().compareTo(probable.getChance());
    }

    Probability getChance();

}
