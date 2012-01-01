package net.exathunk.functional;

public interface Monoid<T> {
    T mempty();
    Monoid<T> mappend(Monoid<T> other);
}