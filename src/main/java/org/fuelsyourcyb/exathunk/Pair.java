package org.fuelsyourcyb.exathunk;

import java.util.Map;

public class Pair<A, B> implements Map.Entry<A, B>,
        EndoFunctor<Pair<A, B>> {
    private A a;
    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public void fmapInto(Func1<Pair<A, B>, Pair<A, B>> f) {
        Pair<A, B> p = f.runFunc(this);
        this.a = p.a;
        this.b = p.b;
    }

    public A getFirst() { return a; }
    public A getKey() { return a; }

    public B getSecond() { return b; }
    public B getValue() { return b; }

    public B setValue(B newB) {
        B oldB = b;
        b = newB;
        return oldB;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Map.Entry)) return false;
        Map.Entry<A, B> e1 = this;
        Map.Entry<A, B> e2 = (Map.Entry<A, B>)o;
        return (e1.getKey()==null ?
                e2.getKey()==null : e1.getKey().equals(e2.getKey()))  &&
                (e1.getValue()==null ?
                        e2.getValue()==null : e1.getValue().equals(e2.getValue()));
    }

    public int hashCode() {
        return (getKey()==null   ? 0 : getKey().hashCode()) ^
                (getValue()==null ? 0 : getValue().hashCode());
    }

}
