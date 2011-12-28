package org.fuelsyourcyb.exathunk;

import java.util.logging.Logger;
import java.util.logging.Level;

public class DefaultEvaluator<T, L, V> implements NTreeEvaluator<T, L, V> {
    private final NTree.Visitor<T, L, Thunk<V>> visitor;

    public DefaultEvaluator(NTree.Visitor<T, L, Thunk<V>> visitor) {
	this.visitor = visitor;
    }

    public V evaluate(NTree<T, L, Thunk<V>> tree) throws VisitException {
	while (true) {
	    if (tree.isEmpty()) {
		throw new VisitException("Empty computation");
	    } else if (tree.isLeaf() && tree.getValue().isDone()) {
		try {
		    return tree.getValue().get();
		} catch (Exception e) {
		    throw new VisitException(e);
		}
	    } else {
		tree.accept(visitor);
		Logger.getLogger("DefaultEvaluator").log(Level.FINE, "After visitor", tree);
	    }
	}
    }
}