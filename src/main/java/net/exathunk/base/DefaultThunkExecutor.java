package net.exathunk.base;

public class DefaultThunkExecutor<T> implements ThunkExecutor<T> {
    public void execute(Thunk<T> thunk) {
        thunk.run();
    }
}
