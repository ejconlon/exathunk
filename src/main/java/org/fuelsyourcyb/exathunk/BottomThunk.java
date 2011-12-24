package org.fuelsyourcyb.exathunk;

public class BottomThunk<StateType, ResultType> implements Thunk<StateType, ResultType> {
    private StateType state;
    private String message;

    public BottomThunk(StateType state, String message) {
	this.state = state;
	this.message = message;
    }

    public boolean isFinished() {
	return true;
    }

    public void step() {}

    public StateType getState() {
	return state;
    }

    public ResultType getResult() {
	throw new ThunkEvaluationException("Bottom: "+message);
    }
}