package net.exathunk.base;

import java.util.concurrent.Callable;
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
                thrown = new ThunkExecutionException(e);
            }
            hasRun = true;
        }
    }

    public Value get() throws ExecutionException {
        if (!hasRun) throw new ThunkExecutionException("Has not run.");
        if (thrown != null) throw thrown;
        return result;
    }

    public boolean isDone() {
        return hasRun;
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
