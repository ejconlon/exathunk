package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ThunkUtils {
    // Do two thunks have the same result?
    public static <Value> boolean statelessEquals(
            Thunk<Value> a, Thunk<Value> b) {
	if (a.isDone() && b.isDone()) {
	    try {
		return a.get().equals(b.get());
	    } catch (InterruptedException e) {
	    } catch (ExecutionException e) {
	    }
	}
	return false;
    }
    
    public static <Type, FuncId, Value> boolean isEvaluable(NTree<Type, FuncId, Thunk<Value>> thunkTree) {
	if (thunkTree.isBranch()) {
	    for (NTree<Type, FuncId, Thunk<Value>> child : thunkTree.getChildren()) {
		if (!child.isLeaf()) return false;
		if (!child.getValue().isDone()) return false;
	    }
	    return true;
	}
	return false;
    }

    public static <Type, FuncId, Value> NTree<Type, FuncId, Thunk<Value>>  makeThunkTree(
 	    ThunkFactory<Type, FuncId, Value> thunkFactory,											 
 	    NTree<Type, FuncId, Value> typedTree) throws UnknownFuncException, ExecutionException {
	if (typedTree.isEmpty()) {
	    throw new UnknownFuncException("Empty computation");
	} else if (typedTree.isLeaf()) {
	    return new NTree<Type, FuncId, Thunk<Value>>(
		typedTree.getType(),
                new PresentThunk<Value>(typedTree.getValue()));
	} else if (typedTree.isTerminalBranch()) {
	    Thunk<Value> thunk = thunkFactory.makeThunk(typedTree.getLabel(), typedTree.extractChildValues());
	    return new NTree<Type, FuncId, Thunk<Value>>(typedTree.getType(), thunk);
	} else {
	    List<NTree<Type, FuncId, Thunk<Value>>> thunkedChildren =
		new ArrayList<NTree<Type, FuncId, Thunk<Value>>>(typedTree.getChildren().size());
	    for (NTree<Type, FuncId, Value> child : typedTree.getChildren()) {
		thunkedChildren.add(makeThunkTree(thunkFactory, child));
	    }
	    return new NTree<Type, FuncId, Thunk<Value>>(typedTree.getType(), typedTree.getLabel(), thunkedChildren);
	}
    }

    public static interface Evaluator<Type, FuncId, Value> extends
        NTree.Visitor<Type, FuncId, Thunk<Value>> {}

    public static class StepEvaluator<Type, FuncId, Value> implements Evaluator<Type, FuncId, Value> {
	private final ThunkFactory<Type, FuncId, Value> factory;

	public StepEvaluator(ThunkFactory<Type, FuncId, Value> factory) {
	    this.factory = factory;
	}

	public List<Value> unthunkValues(NTree<Type, FuncId, Thunk<Value>> tree) throws ExecutionException, InterruptedException {
	    List<Value> params = new ArrayList<Value>(tree.getChildren().size());
	    for (NTree<Type, FuncId, Thunk<Value>> child : tree.getChildren()) {
		Thunk<Value> thunk = child.getValue();
		params.add(thunk.get());
	    }
	    return params;
	}

	public void visit(NTree<Type, FuncId, Thunk<Value>> tree) throws VisitException {
	    try {
		if (ThunkUtils.isEvaluable(tree)) {
		    Thunk<Value> thunk = factory.makeThunk(tree.getLabel(), unthunkValues(tree));
		    tree.setLeaf(tree.getType(), thunk);
		} else if (tree.isLeaf() && !tree.getValue().isDone()) {
		    tree.getValue().step();
		}
	    } catch (ExecutionException e) {
		throw new VisitException(e);
	    } catch (InterruptedException e) {
		throw new VisitException(e);
	    } catch (UnknownFuncException e) {
		throw new VisitException(e);
	    }
	}
    }

}