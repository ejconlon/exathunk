package net.exathunk.base;

import java.util.concurrent.ExecutionException;

public class BottomThunk<Value> implements Thunk<Value> {
    private final ExecutionException exception;

    public BottomThunk(ExecutionException exception) {
        this.exception = exception;
    }

    public void prepare() {}

    public void run() {}

    public Value get() throws ExecutionException {
        throw exception;
    }

    public boolean isDone() {
        return true;  // always already done
    }

    public String toString() {
        return "BottomThunk<"+exception+">";
    }
}