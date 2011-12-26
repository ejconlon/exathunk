package org.fuelsyourcyb.exathunk;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

public class FutureThunk<Value> implements Thunk<Value> {
    private final State state;
    private final Future<Value> future;

    FutureThunk(State state, Future<Value> future) {
	this.state = state;
	this.future = future;
    }

    public void step() {}

    public State getState() {
	return state;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
	return future.cancel(mayInterruptIfRunning);
    }
    
    public boolean isCancelled() {
	return future.isCancelled();
    }

    public boolean isDone() {
	return future.isDone();
    }

    public Value get() throws InterruptedException, ExecutionException {
	return future.get();
    }

    public Value get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
	return future.get(timeout, unit);
    }

}