package org.fuelsyourcyb.exathunk;

// A deferred computation.
// Computation trees chould be implementation-agnostic.
public interface Thunk<Value> {

    // Has the computation finished (or failed)?
    // Until then, getResult will return a null.
    boolean isFinished();

    // An optional callback to perform any work needed
    // to advance the computation.
    // Blocking evaluation of a callback would look
    // something like:
    //   while (!isFinished()) { step(); }
    void step();

    // While thunk implementations should manage the
    // execution of the computation, some exposure of
    // internal state is useful to propagate errors, etc.
    State getState();

    // Result of the computation.  Non-null until completion
    // (Possibly throws runtime ThunkEvaluationException.)
    Value getResult();
}