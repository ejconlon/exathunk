package org.fuelsyourcyb.exathunk;

import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

public class FutureThunk<Value> implements Thunk<Value> {
    private final RunnableFuture<Value> future;

    FutureThunk(RunnableFuture<Value> future) {
	this.future = future;
    }

    public void step() {
	// There is only one step - execute the
	// wrapped future.
	run();
    }

    public void run() {
	future.run();
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