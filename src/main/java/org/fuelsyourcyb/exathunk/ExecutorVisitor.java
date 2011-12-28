package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.concurrent.Executor;

public class ExecutorVisitor<T, L, V> implements NTree.Visitor<T, L, Thunk<V>> {

    private final Executor executor;
    private int count;

    public ExecutorVisitor(Executor executor) {
	this.executor = executor;
	this.count = 0;
    }

    public synchronized void start() {
	count = 0;
    }

    public synchronized void end() {
	while (count > 0) {
	    try {
		this.wait();
	    } catch (InterruptedException ignored) {}
	}
    }

    public void visit(NTree<T, L, Thunk<V>> tree) throws VisitException {
	if (tree.isLeaf() && !tree.getValue().isDone()) {
	    final Thunk<V> thunk = tree.getValue();
	    try {
		incCount();
		executor.execute(new Runnable() {
		    public void run() {
		        try {
			    thunk.run();
			} finally {
 			    decCount();
			}
		    }
		});
	    } catch (Exception e) {
		decCount();
		throw new VisitException(e);
	    }
	}
    }

    private synchronized void incCount() { ++count; }

    private synchronized void decCount() { if(--count <= 0) this.notify(); }

    public synchronized int getCount() { return count; }
}