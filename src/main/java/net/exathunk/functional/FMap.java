package net.exathunk.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FMap {
    public static <A, B> void fmap(Func1<A, B> f, Collection<A> alist, Collection<B> blist) {
        for (A a : alist) {
            blist.add(f.runFunc(a));
        }
    }
    
    public static <A, B> List<B> fmap(Func1<A, B> f, Collection<A> alist) {
        List<B> blist = new ArrayList<>(alist.size());
        fmap(f, alist, blist);
        return blist;
    }
}
