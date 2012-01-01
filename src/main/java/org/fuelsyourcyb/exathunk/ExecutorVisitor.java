package org.fuelsyourcyb.exathunk;

import java.util.concurrent.Executor;

public class ExecutorVisitor<T, L, V> extends ThunkUtils.StepVisitor<T, L, V> {

    private final Executor executor;
    private int count;

    public ExecutorVisitor(ThunkFactory<T, L, V> factory, Executor executor) {
        super(factory);
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

    public void evaluateThunk(final Thunk<V> thunk) throws VisitException {
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

    private synchronized void incCount() { ++count; }

    private synchronized void decCount() { if(--count <= 0) this.notify(); }

    public synchronized int getCount() { return count; }
}