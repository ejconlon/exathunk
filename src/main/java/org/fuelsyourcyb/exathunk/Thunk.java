package org.fuelsyourcyb.exathunk;

public interface Thunk<StateType, ResultType> {
    boolean isFinished();
    void step();
    StateType getState();
    ResultType getResult();
}