package net.exathunk.base;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

public class CallableThunk<Value> implements Thunk<Value> {
    private final Callable<Value> callable;
    private boolean hasRun;
    private Value result;
    private ExecutionException thrown;

    public CallableThunk(Callable<Value> callable) {
        this.callable = callable;
    }

    public void run() {
        if (!hasRun) {
            try {
                result = callable.call();
            } catch (Exception e) {
                thrown = new ThunkEvaluationException(e);
            }
            hasRun = true;
        }
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;  // cannot cancel
    }

    public Value get() throws ExecutionException {
        run();
        if (thrown != null) throw thrown;
        return result;
    }

    public Value get(long timeout, TimeUnit unit) throws ExecutionException {
        run();
        if (thrown != null) throw thrown;
        return result;
    }

    public boolean isCancelled() {
        return false;  // cannot cancel
    }

    public boolean isDone() {
        return result != null;
    }

    public String toString() {
        return "CallableThunk<"+result+">";
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Thunk)) return false;
        return ThunkUtils.statelessEquals(this, (Thunk<Value>)o);
    }
}
