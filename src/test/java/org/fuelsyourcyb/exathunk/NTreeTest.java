package org.fuelsyourcyb.exathunk;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.fuelsyourcyb.exathunk.NTree;
import org.fuelsyourcyb.exathunk.Func1;
import org.fuelsyourcyb.exathunk.Func2;
import org.fuelsyourcyb.exathunk.Unit;
import org.fuelsyourcyb.exathunk.Either;

import java.util.List;
import java.util.ArrayList;

public class NTreeTest {

    public <A> List<A> makePair(A x, A y) {
	List<A> l = new ArrayList<A>();
	l.add(x);
	l.add(y);
	return l;
    }

    public <A> NTree<Unit, A> makeTreePair(A x, A y) {
	NTree<Unit, A> xt, yt;
	xt = new NTree<Unit, A>(x);
        yt = new NTree<Unit, A>(y);
	return new NTree<Unit, A>(Unit.getInstance(), makePair(xt, yt));
    }

    public <A> NTree<Unit, A> makeTreeQuad(A x, A y, A z, A w) {
	return new NTree<Unit, A>(Unit.getInstance(),
				  makePair(makeTreePair(x, y),
					   makeTreePair(z, w)));
    }

    @Test
    public void testEquals() {
	{
	    NTree<Unit, Integer> x = makeTreePair(1, 2);
	    NTree<Unit, Integer> y = makeTreePair(1, 4);
	    System.out.println(x.toString());
	    System.out.println(y.toString());
	    assert(!x.equals(y));
	}

	{
	    NTree<Unit, Integer> x = makeTreePair(1, 2);
	    NTree<Unit, Integer> y = makeTreePair(1, 2);
	    System.out.println(x.toString());
	    System.out.println(y.toString());
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
	NTree<Unit, Integer> x = makeTreePair(1, 2);
	NTree<Unit, Integer> y = makeTreePair(2, 4);
	assert(!x.equals(y));
	x.fmapInto(new Doubler());
	System.out.println(x.toString());
	System.out.println(y.toString());
	assert(x.equals(y));
    }

    private class Halver implements ParametricMutator<Either<Unit, Integer>, NTree<Unit, Integer>> {
	public void mutate(Either<Unit, Integer> x, NTree<Unit, Integer> t) {
	    if (x.isRight() && x.right() > 1) {
		t.setBranch(Unit.getInstance(),
			    makePair(new NTree<Unit, Integer>(x.right()/2),
				     new NTree<Unit, Integer>(x.right()/2)));
	    }
	}
    }
    
    @Test
    public void testBindInto() {
	NTree<Unit, Integer> x = makeTreePair(2, 4);
	NTree<Unit, Integer> y = new NTree<Unit, Integer>(Unit.getInstance(),
							  makePair(makeTreePair(1, 1),
								   makeTreePair(2, 2)));
	assert(!x.equals(y));
	System.out.println("Original: ");
	System.out.println(x.toString());
	x.bindInto(new Halver());
	System.out.println("COMPARE: ");
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
	NTree<Unit, Integer> x = makeTreeQuad(1, 2, 3, 4);
	assert(10 == x.foldl(new Summer(), 0));
	assert(13 == x.foldl(new Summer(), 3));
     }

}
