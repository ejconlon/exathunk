package net.exathunk.base;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

public abstract class NFuncImpl<Type, Value> implements NFunc<Type, Value> {
    private final List<Type> parameterTypes;
    private final Type returnType;

    public NFuncImpl(Type returnType, Type[] paramTypes) {
        this.returnType = returnType;
        List<Type> types = new ArrayList<>(paramTypes.length);
        for (Type paramType : paramTypes) {
            types.add(paramType);
        }
        this.parameterTypes = Collections.unmodifiableList(types);
    }

    public List<Type> getParameterTypes() {
        return parameterTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Thunk<Value> invoke(final List<Value> args) {
        return new CallableThunk<>(new Callable<Value>() {
            public Value call() {
                return subInvoke(args);
            }
        });
    }

    protected abstract Value subInvoke(final List<Value> args);
}
