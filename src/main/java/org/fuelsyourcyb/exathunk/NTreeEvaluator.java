package org.fuelsyourcyb.exathunk;

import java.util.concurrent.ExecutionException;

public interface NTreeEvaluator<T, L, V> {
    V evaluate(NTree<T, L, Thunk<V>> tree) throws VisitException;
}