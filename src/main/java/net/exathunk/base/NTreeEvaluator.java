package net.exathunk.base;

public interface NTreeEvaluator<T, L, V> {
    V evaluate(NTree<T, L, Thunk<V>> tree) throws VisitException;
}