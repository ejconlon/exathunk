package org.fuelsyourcyb.exathunk;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.fuelsyourcyb.exathunk.NTree;
import org.fuelsyourcyb.exathunk.Func1;
import org.fuelsyourcyb.exathunk.Func2;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class NTreeTest {

    public <A> Collection<A> makePair(A x, A y) {
	List<A> l = new ArrayList<A>();
	l.add(x);
	l.add(y);
	return l;
    }

    public <A> NTree<A> makeTreePair(A x, A y) {
	NTree<A> xt, yt;
	xt = new NTree<A>(x);
        yt = new NTree<A>(y);
	return new NTree<A>(makePair(xt, yt));
    }

    public <A> NTree<A> makeTreeQuad(A x, A y, A z, A w) {
	return new NTree<A>(makePair(makeTreePair(x, y),
				     makeTreePair(z, w)));
    }

    @Test
    public void testEquals() {
	{
	    NTree<Integer> x = makeTreePair(1, 2);
	    NTree<Integer> y = makeTreePair(1, 4);
	    assert(!x.equals(y));
	}

	{
	    NTree<Integer> x = makeTreePair(1, 2);
	    NTree<Integer> y = makeTreePair(1, 2);
	    assert(x.equals(y));
	}
    }

    private class Doubler implements Func1<Integer, Integer> {
	public Integer runFunc(Integer x) {
	    return x * 2;
	}
    }

    @Test
    public void testFmapInto() {
	NTree<Integer> x = makeTreePair(1, 2);
	NTree<Integer> y = makeTreePair(2, 4);
	assert(!x.equals(y));
	x.fmapInto(new Doubler());
	assert(x.equals(y));
    }

    private class Halver implements ParametricMutator<Integer, NTree<Integer>> {
	public void mutate(Integer x, NTree<Integer> t) {
	    if (x != null && x > 1) {
		t.setChildren(makePair(new NTree<Integer>(x/2),
				       new NTree<Integer>(x/2)));
	    }
	}
    }
    
    @Test
    public void testBindInto() {
	NTree<Integer> x = makeTreePair(2, 4);
	NTree<Integer> y = new NTree<Integer>(makePair(makeTreePair(1, 1),
						       makeTreeQuad(1, 1, 1, 1)));
	assert(!x.equals(y));
	x.bindInto(new Halver());
	System.out.println(x.toString());
	System.out.println(y.toString());
	assert(x.equals(y));
    }

    private class Summer implements Func2<Integer, Integer, Integer> {
	public Integer runFunc(Integer x, Integer y) {
	    return x + y;
	}
    }

    @Test
    public void testFoldl() {
	NTree<Integer> x = makeTreeQuad(1, 2, 3, 4);
	assert(10 == x.foldl(new Summer(), 0));
	assert(13 == x.foldl(new Summer(), 3));
    }

}
