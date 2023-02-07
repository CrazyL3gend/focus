package ru.crazylegend.focus.util.math.probable;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProbableSet<P extends Probable> implements Iterable<P> {

    private SortedSet<P> set;
    private ProbableSelector<P> selector;
    private Probability common = Probability.zero();

    public ProbableSet(SortedSet<P> set) {
        this.set = set;
        this.selector = ProbableSelector.randomly(set);
    }

    public ProbableSet() {
        this(new TreeSet<>());
    }

    public P getRandomly() {
        return selector.select(common);
    }

    @Override
    public Iterator<P> iterator() {
        return set.iterator();
    }

    public int size() {
        return set.size();
    }

    public boolean add(P probable) {
        final boolean result = set.add(probable);
        common = common.add(probable.getChance());
        return result;
    }

    public void addAll(Collection<? extends P> probables) {
        probables.forEach(this::add);
    }

    public boolean remove(P probable) {
        final boolean result = set.remove(probable);
        common = common.subtract(probable.getChance());
        return result;
    }

}
