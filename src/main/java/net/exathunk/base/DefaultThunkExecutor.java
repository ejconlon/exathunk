package net.exathunk.base;

public class DefaultThunkExecutor<Value> implements ThunkExecutor<Value> {
    public Thunk<Value> submit(Thunk<Value> thunk) {
        thunk.run();
        return thunk;
    }
}
