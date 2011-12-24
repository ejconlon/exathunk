package org.fuelsyourcyb.exathunk;

public interface ThunkFactory<FuncIdType, ParamsType, StateType, ResultType> {
    // createThunk and the evaluator should be treated as if they threw ThunkLookupException
    NTree<Thunk<StateType, ResultType>> createThunk(FuncIdType funcId, ParamsType params);
    ParametricMutator<Thunk<StateType, ResultType>, NTree<Thunk<StateType, ResultType>>> getEvaluator();
}