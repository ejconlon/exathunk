package org.fuelsyourcyb.exathunk;

public interface ThunkFactory<FuncId, Params, State, Label, Value> {
    // createThunk and the evaluator should be treated as if they threw ThunkEvaluationException
    NTree<Label, Thunk<State, Value>> createThunk(FuncId funcId, Params params);
    ParametricMutator<Thunk<State, Value>, NTree<Label, Thunk<State, Value>>> getEvaluator();
}