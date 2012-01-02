package net.exathunk.base;

import java.util.concurrent.ExecutionException;

// A deferred computation.
// Computation trees should be implementation-agnostic.
public interface Thunk<Value> extends Runnable {
    void run();
    
    boolean isDone();
    
    Value get() throws ExecutionException;
}