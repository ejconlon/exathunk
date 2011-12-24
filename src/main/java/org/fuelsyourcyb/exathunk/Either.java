package org.fuelsyourcyb.exathunk;

public class Either<Left, Right> {
    private final Left left;
    private final Right right;

    private Either(Left left, Right right) {
	this.left = left;
	this.right = right;
    }

    public static <L, R> Either<L, R> AsLeft(L left) {
	return new Either<L, R>(left, null);
    }

    public static <L, R> Either<L, R> AsRight(R right) {
	return new Either<L, R>(null, right);
    }

    public boolean isLeft() {
	return left != null;
    }

    public boolean isRight() {
	return right != null;
    }

    public Left left() {
	return left;
    }

    public Right right() {
	return right;
    }

    public boolean equals(Either<Left, Right> o) {
	if (o == null) return false;
	if (left == null) {
	    if (o.left != null) {
		return false;
	    }
	    if (right == null) {
		if (o.right != null) {
		    return false;
		} else {
		    return true;
		}
	    } else {
		return right.equals(o.right);
	    }
	} else {
	    if (o.left == null) {
		return false;
	    }
	    if (right == null) {
		if (o.right != null) {
		    return false;
		} else {
		    return left.equals(o.left);
		}
	    } else {
		return left.equals(o.left) &&
		    right.equals(o.right);
	    }
	}
    }
    
}