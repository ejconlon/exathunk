package net.exathunk.base;

import net.exathunk.functional.Unit;
import net.exathunk.functional.VisitException;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class NTreeTest {

    public <A> List<A> makePair(A x, A y) {
        List<A> l = new ArrayList<>();
        l.add(x);
        l.add(y);
        return l;
    }

    public <A> NTree<Unit, Unit, A> makeTreePair(A x, A y) {
        NTree<Unit, Unit, A> xt, yt;
        xt = new NTree<>(Unit.getInstance(), x);
        yt = new NTree<>(Unit.getInstance(), y);
        return new NTree<>(Unit.getInstance(), Unit.getInstance(), makePair(xt, yt));
    }

    public <A> NTree<Unit, Unit, A> makeTreeQuad(A x, A y, A z, A w) {
        return new NTree<>(Unit.getInstance(), Unit.getInstance(),
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
        public void visit(NTree<Unit, Unit, Integer> t, int depth) {
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
        NTree<Unit, Unit, Integer> y = new NTree<>(Unit.getInstance(), Unit.getInstance(),
                makePair(makeTreePair(1, 1),
                        makeTreePair(2, 2)));
        assert(!x.equals(y));
        System.out.println("Original: ");
        System.out.println(x.toString());
        x.acceptPostorder(new Halver());
        System.out.println("COMPARE: ");
        System.out.println(x.toString());
        System.out.println(y.toString());
        assert(x.equals(y));
    }

}
