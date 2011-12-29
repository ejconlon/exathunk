package org.fuelsyourcyb.exathunk;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class TimeDelayingThunk<Value> implements Thunk<Value> {
    private final long timeout;
    private final TimeUnit timeUnit;
    private boolean doneWaiting;
    private final Thunk<Value> thunk;

    public TimeDelayingThunk(long timeout, TimeUnit timeUnit, Thunk<Value> thunk) {
	this.timeout = timeout;
	this.timeUnit = timeUnit;
	this.doneWaiting = false;
	this.thunk = thunk;
    }

    private void doWait() {
	if (!doneWaiting) {
	    try {
		timeUnit.sleep(timeout);
	    } catch (InterruptedException ignored) {}
	    doneWaiting = true;
	}
    }

    public void run() {
	doWait();
	if (!thunk.isDone()) {
	    thunk.run();
	}
    }

    public boolean cancel(boolean ignored) {
	return thunk.cancel(ignored);
    }

    public boolean isCancelled() {
	return thunk.isCancelled();
    }

    public boolean isDone() {
	return doneWaiting && thunk.isDone();
    }

    public Value get() throws InterruptedException, ExecutionException {
	doWait();
	return thunk.get();
    }

    public Value get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
	doWait();
	return thunk.get(timeout, unit);
    }

    public String toString() {
	return "TimeDelayingThunk<"+doneWaiting+" "+thunk+">";
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
	if (o == null || !(o instanceof Thunk)) return false;
	return ThunkUtils.statelessEquals(this, (Thunk)o);
    }
}
