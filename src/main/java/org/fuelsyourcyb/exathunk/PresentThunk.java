package org.fuelsyourcyb.exathunk;

public class PresentThunk<Value> implements Thunk<Value> {
    private State state;
    private Value result;

    public PresentThunk(State state, Value result) {
	this.state = state;
	this.result = result;
    }

    public boolean isFinished() {
	return true;
    }

    public void step() {}

    public State getState() {
	return state;
    }

    public Value getResult() {
	return result;
    }

    public String toString() {
	return "PresentThunk<"+result+">";
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
	if (o == null || !(o instanceof Thunk)) return false;
	return ThunkUtils.statefulEquals(this, (Thunk<Value>)o);
    }
}
