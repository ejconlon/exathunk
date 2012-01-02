package net.exathunk.base;

import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ExecutionException;

public class FutureThunk<Value> implements Thunk<Value> {
    private final RunnableFuture<Value> future;

    FutureThunk(RunnableFuture<Value> future) {
        this.future = future;
    }

    public void prepare() {}

    public void run() {
        future.run();
    }

    public boolean isDone() {
        return future.isDone();
    }

    public Value get() throws ExecutionException {
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new ThunkExecutionException(e);
        }
    }
}