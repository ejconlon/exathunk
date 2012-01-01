package net.exathunk.base;

import java.util.concurrent.RunnableFuture;

// A deferred computation.
// Computation trees should be implementation-agnostic.
public interface Thunk<Value> extends RunnableFuture<Value> {

}