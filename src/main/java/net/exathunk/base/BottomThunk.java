package net.exathunk.base;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

public class BottomThunk<Value> implements Thunk<Value> {
    private final ExecutionException exception;

    public BottomThunk(ExecutionException exception) {
        this.exception = exception;
    }

    public void run() {}

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;  // always already done
    }

    public Value get() throws ExecutionException {
        throw exception;
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
        return "BottomThunk<"+exception+">";
    }
}