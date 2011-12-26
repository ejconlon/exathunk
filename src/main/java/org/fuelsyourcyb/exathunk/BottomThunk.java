package org.fuelsyourcyb.exathunk;

public class BottomThunk<Value> implements Thunk<Value> {
    private State state;
    private String message;

    public BottomThunk(State state, String message) {
	this.state = state;
	this.message = message;
    }

    public boolean isFinished() {
	return true;
    }

    public void step() {}

    public State getState() {
	return state;
    }

    public Value getResult() {
	throw new ThunkEvaluationException("Bottom: "+message);
    }
}