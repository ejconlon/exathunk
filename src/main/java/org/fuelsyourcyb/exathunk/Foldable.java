package org.fuelsyourcyb.exathunk;

public interface Foldable<A> {
    <B> B foldl(Func2<B, A, B> f, B initial);
}