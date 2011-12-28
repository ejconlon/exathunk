package org.fuelsyourcyb.exathunk;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

public class BottomThunk<Value> implements Thunk<Value> {
    private String message;
    private Throwable throwable;

    public BottomThunk(String message, Throwable throwable) {
	this.message = message;
	this.throwable = throwable;
    }

    public void step() {}

    public boolean cancel(boolean mayInterruptIfRunning) {
	return false;  // always already done
    }

    public Value get() throws ExecutionException {
	throw new ThunkEvaluationException(message, throwable);
    }

    public Value get(long timeout, TimeUnit unit) throws ExecutionException {
	return get();
    }

    public boolean isCancelled() {
	return false;  // always already done
    }

    public boolean isDone() {
	return true;  // always already done
    }

    public String toString() {
	return "BottomThunk<"+message+">";
    }
}