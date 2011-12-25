package org.fuelsyourcyb.exathunk;

public final class Unit implements TypeFactory<Unit> {
    private static final Unit INSTANCE = new Unit();
    public static Unit getInstance() { return INSTANCE; }
    public Unit makeInstance() { return INSTANCE; }
    private Unit() {}
    public String toString() { return "()"; }
}