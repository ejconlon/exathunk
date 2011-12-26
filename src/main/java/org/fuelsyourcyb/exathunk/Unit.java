package org.fuelsyourcyb.exathunk;

public final class Unit implements State, StateFactory<State> {
    private static final Unit INSTANCE = new Unit();
    public static Unit getInstance() { return INSTANCE; }
    public Unit makeInitialState() { return INSTANCE; }
    private Unit() {}
    public String toString() { return "()"; }
}