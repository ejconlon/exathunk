package org.fuelsyourcyb.exathunk;

import java.util.List;

// An abstract interface to capture thunk creation and evaluation.
// Type parameters:
//   FuncId: An identifier for function addressing/creation/resolution/etc
//           Could be a name or address.
//   Value:  A boxed value for the result of any thunked computation or
//           for function parameters.
//
// Calling code should be aware of RuntimeExceptions in the form of
// ThunkEvaluationExceptions.  I may possibly try checked annotations later,
// but it wasn't straightforward to annotate them in double-dispatched code...
//
// The idea here is to capture the structure of computation and tree rewriting
// while abstracting away the implementation of Thunk creation and evluation.
public interface ThunkFactory<FuncId, Value> {

    boolean knowsFunc(FuncId funcId);

    // Create a leaf Thunk representing a deferred/remote computation.
    Thunk<Value> makeThunk(FuncId funcId, List<Value> params);

    StateFactory<State> getStateFactory();
}