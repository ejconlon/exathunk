package org.fuelsyourcyb.exathunk;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class StepDelayingThunk<Value> implements Thunk<Value> {
    private final Integer numStepsNeeded;
    private Integer numStepsTaken;
    private final Thunk<Value> thunk;

    public StepDelayingThunk(Integer numStepsNeeded, Thunk<Value> thunk) {
        this.numStepsNeeded = numStepsNeeded;
        this.thunk = thunk;
        this.numStepsTaken = 0;
    }

    public void run() {
        while (!isDone()) {
            if (numStepsTaken < numStepsNeeded) {
                ++numStepsTaken;
            } else {
                thunk.run();
            }
        }
    }

    public boolean cancel(boolean ignored) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return numStepsTaken >= numStepsNeeded && thunk.isDone();
    }

    public Value get() throws InterruptedException, ExecutionException {
        if (numStepsTaken >= numStepsNeeded) {
            return thunk.get();
        } else {
            throw new InterruptedException();
        }
    }

    public Value get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (numStepsTaken >= numStepsNeeded) {
            return thunk.get(timeout, unit);
        } else {
            throw new InterruptedException();
        }
    }

    public String toString() {
        return "StepDelayingThunk<"+numStepsTaken+"/"+numStepsNeeded+" "+thunk+">";
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Thunk)) return false;
        return ThunkUtils.statelessEquals(this, (Thunk)o);
    }
}
