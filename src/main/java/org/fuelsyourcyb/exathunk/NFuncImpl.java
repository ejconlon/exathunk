package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public abstract class NFuncImpl<Type, Value> implements NFunc<Type, Value> {
    private final List<Type> parameterTypes;
    private final Type returnType;

    public NFuncImpl(Type returnType, Type[] paramTypes) {
	this.returnType = returnType;
	List<Type> types = new ArrayList<Type>(paramTypes.length);
	for (int i = 0; i < paramTypes.length; ++i) {
	    types.add(paramTypes[i]);
	}
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
