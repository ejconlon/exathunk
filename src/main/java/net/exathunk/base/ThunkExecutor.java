package net.exathunk.base;

import java.util.concurrent.ExecutionException;

public interface ThunkExecutor<Value> {
    void execute(Thunk<Value> thunk) throws ExecutionException;
}
