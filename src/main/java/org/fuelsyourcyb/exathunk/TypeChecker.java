package org.fuelsyourcyb.exathunk;

public interface TypeChecker<Type, FromValue, ToValue> {
    boolean check(Type type, FromValue fromValue);

    ToValue convert(Type type, FromValue fromValue) throws TypeException;
}