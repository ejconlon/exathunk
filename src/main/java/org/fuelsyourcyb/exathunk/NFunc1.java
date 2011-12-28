package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public abstract class NFunc1<Type, Value> implements NFunc<Type, Value> {
    private final List<Type> parameterTypes;
    private final Type returnType;

    public NFunc1(Type returnType, Type paramType) {
	this.returnType = returnType;
	List<Type> types = new ArrayList<Type>(1);
	types.add(paramType);
	this.parameterTypes = Collections.unmodifiableList(types);
    }

    public List<Type> getParameterTypes() {
	return parameterTypes;
    }

    public Type getReturnType() {
	return returnType;
    }
    
    public abstract Value invoke(List<Value> args) throws ExecutionException;
}
