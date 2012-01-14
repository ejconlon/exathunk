package net.exathunk.base;

public interface ThunkExecutor<Value> {
    // Return a thunk containing the evaluation of the given thunk.
    // It is not guaranteed that the thunks reference the same object.
    Thunk<Value> submit(Thunk<Value> thunk);
}
