package org.fuelsyourcyb.exathunk;

import java.util.concurrent.TimeUnit;

public class PresentThunk<Value> implements Thunk<Value> {
    private State state;
    private Value result;

    public PresentThunk(State state, Value result) {
	this.state = state;
	this.result = result;
    }

    public void step() {}

    public State getState() {
	return state;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
	return false;  // always already done
    }

    public Value get() {
	return result;
    }

    public Value get(long timeout, TimeUnit unit) {
	return result;
    }

    public boolean isCancelled() {
	return false;  // always already done
    }

    public boolean isDone() {
	return true;  // always already done
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
