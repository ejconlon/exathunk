package net.exathunk.base;

public class DefaultThunkExecutor<T> implements ThunkExecutor<T> {
    public Thunk<T> submit(Thunk<T> thunk) {
        thunk.run();
        return thunk;
    }
}
