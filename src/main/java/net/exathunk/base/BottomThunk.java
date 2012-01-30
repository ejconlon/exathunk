package net.exathunk.base;

import java.util.concurrent.ExecutionException;

public class BottomThunk<Value> implements Thunk<Value> {
    private final Throwable exception;

    public BottomThunk(Throwable exception) {
        this.exception = exception;
    }

    public void run() {}

    public Value get() throws ExecutionException {
        throw new ExecutionException(exception);
    }

    public boolean isDone() {
        return true;  // always already done
    }

    public String toString() {
        return "BottomThunk<"+exception+">";
    }
}