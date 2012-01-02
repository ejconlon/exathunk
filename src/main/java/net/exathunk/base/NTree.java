package net.exathunk.base;

import net.exathunk.functional.VisitException;

import java.util.List;
import java.util.ArrayList;

// A tree with arbitrary numbers of children and separate types for
// inner node labels and leaf node values.
public class NTree<T, L, V> {

    public static interface Visitor<T, L, V> {
        void visit(NTree<T, L, V> tree) throws VisitException;

        void start();

        void end();
    }

    // Should not be null in any case
    private T type;
    // Null iff this is a leaf
    private L label;
    // Null iff this is a branch
    private V value;
    // Null iff this is a leaf
    private List<NTree<T, L, V>> children;

    public NTree() {
        setEmpty();
    }

    public NTree(T type, V value) {
        setLeaf(type, value);
    }

    public NTree(T type, L label, List<NTree<T, L, V>> children) {
        setBranch(type, label, children);
    }

    public void accept(Visitor<T, L, V> visitor) throws VisitException {
        try {
            visitor.start();
            subAccept(visitor);
        } finally {
            visitor.end();
        }
    }

    protected void subAccept(Visitor<T, L, V> visitor) throws VisitException {
        if (children != null) {
            for (NTree<T, L, V> child : children) {
                child.subAccept(visitor);
            }
            // Clean up any empty nodes.
            children.remove(new NTree<T, L, V>());
        }
        visitor.visit(this);
    }

    // All nodes will have a non-null type.
    public T getType() {
        return type;
    }

    // Branches will have a non-null label.
    public L getLabel() {
        return label;
    }

    // Leaves will have a non-null value.
    public V getValue() {
        return value;
    }

    // Inner node will have a non-null child list (possibly empty)
    public List<NTree<T, L, V>> getChildren() {
        return children;
    }

    public boolean isTerminalBranch() {
        if (isBranch()) {
            for (NTree<T, L, V> child : children) {
                if (!child.isLeaf()) return false;
            }
            return true;
        }
        return false;
    }

    // Must be a terminal branch
    public List<T> extractChildTypes() {
        List<T> types = new ArrayList<>(children.size());
        for (NTree<T, L, V> child : children) {
            types.add(child.getType());
        }
        return types;
    }

    // Must be a terminal branch
    public List<V> extractChildValues() {
        List<V> values = new ArrayList<>(children.size());
        for (NTree<T, L, V> child : children) {
            values.add(child.getValue());
        }
        return values;
    }

    // Turn this node into a leaf with the given value.
    // Will discard children if present.
    // value should not be null.
    public void setLeaf(T type, V value) {
        this.type = type;
        this.label = null;
        this.value = value;
        this.children = null;
    }

    // Turn this node into an inner node.
    // Neither argument should be null.
    public void setBranch(T type, L label, List<NTree<T, L, V>> children) {
        this.type = type;
        this.label = label;
        this.value = null;
        this.children = children;
    }

    // "Delete" this node. bindInto will remove it from
    // a parent node's child list.
    public void setEmpty() {
        this.type = null;
        this.label = null;
        this.value = null;
        this.children = null;
    }

    public boolean isLeaf() {
        return this.value != null;
    }

    public boolean isBranch() {
        return label != null;
    }

    public boolean isEmpty() {
        return !isLeaf() && !isBranch();
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == null || !(o instanceof NTree)) return false;
        NTree<T, L, V> t = (NTree<T, L, V>)o;
        if (type != null) {
            if (!type.equals(t.type)) {
                return false;
            }
        } else if (t.type != null) {
            return false;
        }
        if (label != null) {
            if (!label.equals(t.label)) {
                return false;
            }
        } else if (t.label != null) {
            return false;
        }
        if (value != null) {
            if (!value.equals(t.value)) {
                return false;
            }
        } else if (t.value != null) {
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
        StringBuilder s = new StringBuilder();
        s.append("{ ");
        s.append("type = "+type+", ");
        if (label != null) {
            s.append("label = "+label+", ");
        }
        if (value != null) {
            s.append("value = "+value+" ");
        }
        if (children != null) {
            s.append("children = [ ");
            for (NTree<T, L, V> child : children) {
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