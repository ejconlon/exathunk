package net.exathunk.functional;

public final class Unit {
    private static final Unit INSTANCE = new Unit();
    public static Unit getInstance() { return INSTANCE; }
    private Unit() {}
    public String toString() { return "()"; }
}