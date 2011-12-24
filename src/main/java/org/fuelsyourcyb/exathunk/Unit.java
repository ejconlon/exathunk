package org.fuelsyourcyb.exathunk;

public final class Unit {
    private static final Unit INSTANCE = new Unit();
    public static Unit getInstance() { return INSTANCE; }
    private Unit() {}
    public String toString() { return "()"; }
    public boolean equals(Object o) {
	return o != null && o instanceof Unit;
    }
}