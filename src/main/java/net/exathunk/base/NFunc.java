package net.exathunk.base;

import java.util.List;

public interface NFunc<Type, Label, Value> {
    Type getReturnType();
    List<Type> getParameterTypes();
    List<Strictness> getStrictnesses();
    Thunk<Value> invoke(ThunkFactory<Type, Label, Value> thunkFactory,
                        ThunkExecutor<Value> executor, NTree<Type, Label, Value> args);
}