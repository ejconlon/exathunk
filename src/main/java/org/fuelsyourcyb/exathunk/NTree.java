package org.fuelsyourcyb.exathunk;

import java.util.Collection;
import java.util.Iterator;

public class NTree<L, V> implements EndoFunctor<Either<L, V>>,
				    Foldable<V>,
				    EndoMonad<Either<L, V>, NTree<L, V>> {
    private Either<L, V> labelOrValue;
    private Collection<NTree<L, V>> children;

    public NTree() {
	this.labelOrValue = null;
	this.children = null;
    }

    public NTree(V value) {
	this.labelOrValue = Either.AsRight(value);
	this.children = null;
    }

    public  NTree(L label, Collection<NTree<L, V>> children) {
	this.labelOrValue = Either.AsLeft(label);
	this.children = children;
    }

    public void bindInto(ParametricMutator<Either<L, V>, NTree<L, V>> f) {
	f.mutate(labelOrValue, this);
	if (children != null) {
	    if (labelOrValue.isLeft()) {
		for (NTree<L, V> child : children) {
		    child.bindInto(f);
		}
		children.remove(new NTree<L, V>());
		if (children.isEmpty()) {
		    children = null;
		}
	    } else {
		children = null;
	    }
	}
    }

    public void fmapInto(Func1<Either<L, V>, Either<L, V>> f) {
	if (labelOrValue != null) {
	    labelOrValue = f.runFunc(labelOrValue);
	}
	if (children != null) {
	    if (labelOrValue.isLeft()) {
		for (NTree<L, V> child : children) {
		    child.fmapInto(f);
		}
	    } else {
		children = null;
	    }
	}
    }
    
    // foldl: (((1 + 2) + 3) + 4)
    public <B> B foldl(Func2<B, V, B> f, B initial) {
	if (labelOrValue != null && labelOrValue.isRight()) {
	    return f.runFunc(initial, labelOrValue.right());
	} else if (children != null) {
	    for (NTree<L, V> child : children) {
		initial = child.foldl(f, initial);
	    }
	    return initial;
	} else {
	    return initial;
	}
    }

    public V getValue() {
	return labelOrValue.right();
    }

    public L getLabel() {
	return labelOrValue.left();
    }

    public Collection<NTree<L, V>> getChildren() {
	return children;
    }

    public void setValue(V value) {
	this.labelOrValue = Either.AsRight(value);
	this.children = null;
    }

    public void setInner(L label, Collection<NTree<L, V>> children) {
	this.labelOrValue = Either.AsLeft(label);
	this.children = children;
    }

    public void makeEmpty() {
	this.labelOrValue = null;
	this.children = null;
    }

    public boolean isLeaf() {
	return labelOrValue != null &&
	    labelOrValue.isRight();
    }

    public boolean isInner() {
	return children != null || !isLeaf();
    }

    public boolean isEmpty() {
	return this.labelOrValue == null &&
	    this.children == null;
    }

    public boolean equals(NTree<L, V> o) {
	if (o == null) return false;
	if (labelOrValue != null) {
	    if (!labelOrValue.equals(o.labelOrValue)) {
		return false;
	    }
	}
	if (children != null) {
	    if (o.children == null ||
		children.size() != o.children.size()) {
		return false;
	    }
	    Iterator<NTree<L, V>> it1 = children.iterator();
	    Iterator<NTree<L, V>> it2 = o.children.iterator();
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
	if (labelOrValue != null) {
	    if (labelOrValue.isRight()) {
		s.append("value = "+labelOrValue.right());
	    } else {
		s.append("label = "+labelOrValue.left()+" ");
	    }
	}
	if (children != null) {
	    s.append("children = [ ");
	    for (NTree<L, V> child : children) {
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