package org.fuelsyourcyb.exathunk;

public interface EndoFunctor<A> {
    void fmapInto(Func1<A, A> f);
}