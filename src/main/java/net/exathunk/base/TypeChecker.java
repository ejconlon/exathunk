package net.exathunk.base;

public interface TypeChecker<Type, FromValue, ToValue> {
    boolean canCast(Type fromType, Type toType);

    ToValue cast(Type toType, FromValue fromValue) throws TypeException;
}