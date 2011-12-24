package org.fuelsyourcyb.exathunk;

import java.util.Collection;
import java.util.Iterator;

public class NTree<A> implements EndoFunctor<A>,
                                 Foldable<A>,
				 EndoMonad<A, NTree<A>> {
    private A value;
    private Collection<NTree<A>> children;

    public NTree() {
	this.value = null;
	this.children = null;
    }

    public NTree(A value) {
	this.value = value;
	this.children = null;
    }

    public  NTree(Collection<NTree<A>> children) {
	this.value = null;
	this.children = children;
    }

    public void bindInto(ParametricMutator<A, NTree<A>> f) {
	f.mutate(value, this);
	if (children != null) {
	    for (NTree<A> child : children) {
		child.bindInto(f);
	    }
	    children.remove(new NTree());
	    if (children.isEmpty()) {
		children = null;
	    }
	}
    }

    public void fmapInto(Func1<A, A> f) {
	if (value != null) {
	    value = f.runFunc(value);
	} else if (children != null) {
	    for (NTree<A> child : children) {
		child.fmapInto(f);
	    }
	}
    }
    
    // foldl: (((1 + 2) + 3) + 4)
    public <B> B foldl(Func2<B, A, B> f, B initial) {
	if (value != null) {
	    return f.runFunc(initial, value);
	} else if (children != null) {
	    for (NTree<A> child : children) {
		initial = child.foldl(f, initial);
	    }
	    return initial;
	} else {
	    return initial;
	}
    }

    public A getValue() {
	return value;
    }

    public Collection<NTree<A>> getChildren() {
	return children;
    }

    public void setValue(A value) {
	this.value = value;
	this.children = null;
    }

    public void setChildren(Collection<NTree<A>> children) {
	this.value = null;
	this.children = children;
    }

    public void makeEmpty() {
	this.value = null;
	this.children = null;
    }

    public boolean isLeaf() {
	return this.value != null;
    }

    public boolean isInner() {
	return this.children != null;
    }

    public boolean isEmpty() {
	return this.value == null &&
	    this.children == null;
    }

    public boolean equals(NTree<A> o) {
	if (o == null) return false;
	if (value != null) {
	    return value == o.value;
	} else if (children != null) {
	    if (o.children == null ||
		children.size() != o.children.size()) {
		return false;
	    }
	    Iterator<NTree<A>> it1 = children.iterator();
	    Iterator<NTree<A>> it2 = o.children.iterator();
	    while (it1.hasNext()) {
		if (!it1.next().equals(it2.next())) {
		    return false;
		}
	    }
	    return true;
	} else {
	    return true;
	}
    }

    public String toString() {
	StringBuffer s = new StringBuffer();
	s.append("{ ");
	if (value != null) {
	    s.append("value = "+value);
	} else if (children != null) {
	    s.append("children = [ ");
	    for (NTree<A> child : children) {
		s.append(child.toString());
		s.append(", ");
	    }
	    if (!children.isEmpty()) {
		s.deleteCharAt(s.length()-1);
		s.deleteCharAt(s.length()-1);
	    }
	    s.append(" ]");
	}
	s.append(" }");
	return s.toString();
    }
}