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

    public <A> NTree<Unit, Unit, A> makeTreePair(A x, A y) {
	NTree<Unit, Unit, A> xt, yt;
	xt = new NTree<Unit, Unit, A>(Unit.getInstance(), x);
        yt = new NTree<Unit, Unit, A>(Unit.getInstance(), y);
	return new NTree<Unit, Unit, A>(Unit.getInstance(), Unit.getInstance(), makePair(xt, yt));
    }

    public <A> NTree<Unit, Unit, A> makeTreeQuad(A x, A y, A z, A w) {
	return new NTree<Unit, Unit, A>(Unit.getInstance(), Unit.getInstance(),
					makePair(makeTreePair(x, y),
						 makeTreePair(z, w)));
    }

    @Test
    public void testEquals() {
	{
	    NTree<Unit, Unit, Integer> x = makeTreePair(1, 2);
	    NTree<Unit, Unit, Integer> y = makeTreePair(1, 4);
	    System.out.println(x.toString());
	    System.out.println(y.toString());
	    assert(!x.equals(y));
	}

	{
	    NTree<Unit, Unit, Integer> x = makeTreePair(1, 2);
	    NTree<Unit, Unit, Integer> y = makeTreePair(1, 2);
	    System.out.println(x.toString());
	    System.out.println(y.toString());
	    assert(x.equals(y));
	}
    }

    private class Halver implements NTree.Visitor<Unit, Unit, Integer> {
	public void visit(NTree<Unit, Unit, Integer> t) {
	    if (t.isLeaf()) {
		t.setBranch(Unit.getInstance(), Unit.getInstance(),
			    makePair(new NTree<Unit, Unit, Integer>(Unit.getInstance(), t.getValue()/2),
				     new NTree<Unit, Unit, Integer>(Unit.getInstance(), t.getValue()/2)));
	    }
	}
    }
    
    @Test
    public void testAccept() throws VisitException {
	NTree<Unit, Unit, Integer> x = makeTreePair(2, 4);
	NTree<Unit, Unit, Integer> y = new NTree<Unit, Unit, Integer>(Unit.getInstance(), Unit.getInstance(),
								      makePair(makeTreePair(1, 1),
									       makeTreePair(2, 2)));
	assert(!x.equals(y));
	System.out.println("Original: ");
	System.out.println(x.toString());
	x.accept(new Halver());
	System.out.println("COMPARE: ");
	System.out.println(x.toString());
	System.out.println(y.toString());
	assert(x.equals(y));
    }

}
