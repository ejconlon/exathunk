package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface NFunc<Type, Value> {
    List<Type> getParameterTypes();
    Type getReturnType();
    Value invoke(List<Value> args) throws ExecutionException;
}