package net.exathunk.base;

import net.exathunk.functional.VisitException;

public interface NTreeEvaluator<T, L, V> {
    V evaluate(NTree<T, L, Thunk<V>> tree) throws VisitException;
}