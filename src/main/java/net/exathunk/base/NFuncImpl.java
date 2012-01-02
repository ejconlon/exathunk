package net.exathunk.base;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class NFuncImpl<Type, Label, Value> implements NFunc<Type, Label, Value> {
    
    private final Type returnType;
    private final List<Type> parameterTypes;
    private final List<Strictness> strictnesses;

    public NFuncImpl(Type returnType, Type[] parameterTypes, Strictness[] strictnesses) {
        this.returnType = returnType;
        this.parameterTypes = listify(parameterTypes);
        this.strictnesses = listify(strictnesses);
        assert parameterTypes.length == strictnesses.length;
    }

    private static <T> List<T> listify(T[] objs) {
        List<T> list = new ArrayList<>(objs.length);
        for (T obj : objs) list.add(obj);
        return Collections.unmodifiableList(list);
    }
    
    public List<Type> getParameterTypes() {
        return parameterTypes;
    }

    public List<Strictness> getStrictnesses() {
        return strictnesses;
    }

    public Type getReturnType() {
        return returnType;
    }

    public abstract Thunk<Value> invoke(ThunkFactory<Type, Label, Value> thunkFactory,
                                        ThunkExecutor<Value> executor, NTree<Type, Label, Value> args);
}
