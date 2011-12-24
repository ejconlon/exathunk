package org.fuelsyourcyb.exathunk;

public interface Monoid<T> {
    T mempty();
    Monoid<T> mappend(Monoid<T> other);
}