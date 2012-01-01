package net.exathunk.base;

import java.util.List;

public interface NFunc<Type, Value> {
    List<Type> getParameterTypes();
    Type getReturnType();
    Thunk<Value> invoke(List<Value> args);
}