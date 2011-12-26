package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.Iterator;

// A tree with arbirary numbers of children and separate types for
// inner node labels and leaf node values.
// Supports folding and mutating-fmapping on leaves or mutating-monadic-binding
// from bottom-up.
public class NTree<L, V> implements EndoFunctor<V>,
				    Foldable<V>,
				    EndoMonad<Either<L, V>, NTree<L, V>> {

    // Should not be null in any case.
    private Either<L, V> labelOrValue;
    // Should be null iff this is a leaf node.
    private List<NTree<L, V>> children;

    public NTree() {
	this.labelOrValue = null;
	this.children = null;
    }

    public NTree(V value) {
	this.labelOrValue = Either.AsRight(value);
	this.children = null;
    }

    public NTree(L label, List<NTree<L, V>> children) {
	this.labelOrValue = Either.AsLeft(label);
	this.children = children;
    }

    // Bind f into this monadic tree (bottom-up).
    public void bindInto(ParametricMutator<Either<L, V>, NTree<L, V>> f) {
	if (children != null) {
	    for (NTree<L, V> child : children) {
		child.bindInto(f);
	    }
	    // clean up any empty nodes. this may technically break
	    // some monad laws but I'm not going to worry about it.
	    children.remove(new NTree<L, V>());
	}
	f.mutate(labelOrValue, this);
    }

    // Fmaps into leaves.
    public void fmapInto(Func1<V, V> f) {
	if (labelOrValue != null && labelOrValue.isRight()) {
	    labelOrValue = Either.AsRight(f.runFunc(labelOrValue.right()));
	}
	if (children != null) {
	    for (NTree<L, V> child : children) {
		child.fmapInto(f);
	    }
	}
    }

    // Folds along leaves.
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

    // Leaves will have a non-null value.
    public V getValue() {
	return labelOrValue.right();
    }

    // Inner nodes will have a non-null label.
    public L getLabel() {
	return labelOrValue.left();
    }

    // Inner node will have a non-null child list (possibly empty)
    public List<NTree<L, V>> getChildren() {
	return children;
    }

    // Turn this node into a leaf with the given value.
    // Will discard children if present.
    // value should not be null.
    public void setValue(V value) {
	this.labelOrValue = Either.AsRight(value);
	this.children = null;
    }

    // Turn this node into an inner node.
    // Neither argument should be null.
    public void setBranch(L label, List<NTree<L, V>> children) {
	this.labelOrValue = Either.AsLeft(label);
	this.children = children;
    }

    // "Delete" this node. bindInto will remove it from
    // a parent node's child list.
    public void makeEmpty() {
	this.labelOrValue = null;
	this.children = null;
    }

    public boolean isLeaf() {
	return labelOrValue != null &&
	    labelOrValue.isRight();
    }

    public boolean isBranch() {
	return children != null || !isLeaf();
    }

    public boolean isEmpty() {
	return this.labelOrValue == null &&
	    this.children == null;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
	if (o == null || !(o instanceof NTree)) return false;
	NTree<L, V> t = (NTree<L, V>)o;
	if (labelOrValue != null) {
	    if (!labelOrValue.equals(t.labelOrValue)) {
		return false;
	    }
	} else if (t.labelOrValue != null) {
	    return false;
	}
	if (children != null) {
	    if (t.children == null ||
		children.size() != t.children.size()) {
		return false;
	    }
	    for (int i = 0; i < children.size(); ++i) {
		if (!children.get(i).equals(t.children.get(i))) {
		    return false;
		}
	    }
	} else if (t.children != null) {
	    return false;
	}
	return true;
    }

    // TODO(ejconlon) Prettify this.
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