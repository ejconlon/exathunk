package net.exathunk.base;

import java.util.List;
import java.util.concurrent.ExecutionException;

// An abstract interface to capture thunk creation and evaluation.
// Type parameters:
//   Type:   Represents the type of an argument.  There should be a TypeChecker
//           instance that ensures Values represent Types.
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
public interface ThunkFactory<Type, FuncId, Value> {

    boolean knowsFunc(FuncId funcId);

    List<Type> getParameterTypes(FuncId funcId) throws UnknownFuncException;

    Type getReturnType(FuncId funcId) throws UnknownFuncException;

    // Create a leaf Thunk representing a deferred/remote computation.
    Thunk<Value> makeThunk(FuncId funcId, List<Value> params) throws UnknownFuncException, ExecutionException;
}
