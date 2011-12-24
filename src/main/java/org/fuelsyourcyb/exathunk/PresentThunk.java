package org.fuelsyourcyb.exathunk;

public class PresentThunk<StateType, ResultType> implements Thunk<StateType, ResultType> {
    private StateType state;
    private ResultType result;

    public PresentThunk(StateType state, ResultType result) {
	this.state = state;
	this.result = result;
    }

    public boolean isFinished() {
	return true;
    }

    public void step() {}

    public StateType getState() {
	return state;
    }

    public ResultType getResult() {
	return result;
    }
}