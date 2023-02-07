package ru.crazylegend.focus.util.math.probable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ProbableList<P extends Probable> implements Iterable<P> {

    private List<P> list;
    private ProbableSelector<P> selector;
    private Probability common = Probability.zero();

    public ProbableList(List<P> list) {
        this.list = list;
        this.selector = ProbableSelector.randomly(list);
    }

    public ProbableList() {
        this(new ArrayList<>());
    }

    public P getRandomly() {
        return selector.select(common);
    }

    @Override
    public Iterator<P> iterator() {
        return list.iterator();
    }

    public int size() {
        return list.size();
    }

    public boolean add(P probable) {
        list.add(probable);
        common = common.add(probable.getChance());
        return true;
    }

    public void addAll(Collection<? extends P> probables) {
        probables.forEach(this::add);
    }

    public boolean remove(P probable) {
        boolean result = list.remove(probable);
        common = common.subtract(probable.getChance());
        return result;
    }

}
