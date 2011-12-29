package org.fuelsyourcyb.exathunk;

import java.util.concurrent.RunnableFuture;

// A deferred computation.
// Computation trees chould be implementation-agnostic.
public interface Thunk<Value> extends RunnableFuture<Value> {

    // TODO(ejconlon) Implement this when needed.
    // While thunk implementations should manage the
    // execution of the computation, some exposure of
    // internal state is useful to propagate errors, etc.
    // State getState();
}