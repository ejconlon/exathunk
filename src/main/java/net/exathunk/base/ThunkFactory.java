package net.exathunk.base;

import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

import java.util.concurrent.ExecutionException;

// An abstract interface to capture thunk creation and evaluation.
//
// Calling code should be aware of RuntimeExceptions in the form of
// ThunkEvaluationExceptions.  I may possibly try checked annotations later,
// but it wasn't straightforward to annotate them in double-dispatched code...
//
// The idea here is to capture the structure of computation and tree rewriting
// while abstracting away the implementation of Thunk creation and evluation.
public interface ThunkFactory {

    boolean knowsFunc(FuncId funcId);

    NFunc getFunc(FuncId funcId) throws  UnknownFuncException;

    // Create a leaf Thunk representing a deferred/remote computation.
    Thunk<VarCont> makeThunk(ThunkExecutor<VarCont> executor,
                             NTree<VarContType, FuncId, VarCont> tree) throws UnknownFuncException, ExecutionException;
}
