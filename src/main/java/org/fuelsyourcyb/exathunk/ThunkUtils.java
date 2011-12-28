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
    
    // Do two thunks have the same result AND the same
    // internal state?
    public static <Value> boolean statefulEquals(
            Thunk<Value> a, Thunk<Value> b) {
	if (!a.getState().equals(b.getState())) return false;
	return statelessEquals(a, b);
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
 	    NTree<Type, FuncId, Value> typedTree,
	    ThunkFactory<Type, FuncId, Value> thunkFactory) throws UnknownFuncException {
	if (typedTree.isEmpty()) {
	    throw new UnknownFuncException("Empty computation");
	} else if (typedTree.isLeaf()) {
	    State initState = thunkFactory.getStateFactory().makeInitialState();
	    return new NTree<Type, FuncId, Thunk<Value>>(
		typedTree.getType(),
                new PresentThunk<Value>(initState, typedTree.getValue()));
	} else if (typedTree.isTerminalBranch()) {
	    Pair<Type, Thunk<Value>> pair = thunkFactory.makeThunk(typedTree.getLabel(), typedTree.extractChildPairs());
	    return new NTree<Type, FuncId, Thunk<Value>>(pair.getFirst(), pair.getSecond());
	} else {
	    List<NTree<Type, FuncId, Thunk<Value>>> thunkedChildren = new ArrayList<NTree<Type, FuncId, Thunk<Value>>>(typedTree.getChildren().size());
	    for (NTree<Type, FuncId, Value> child : typedTree.getChildren()) {
		thunkedChildren.add(makeThunkTree(child, thunkFactory));
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

	public List<Pair<Type, Value>> extractThunkValues(NTree<Type, FuncId, Thunk<Value>> tree) throws ExecutionException {
	    List<Pair<Type, Value>> params = new ArrayList<Pair<Type, Value>>(tree.getChildren().size());
	    for (NTree<Type, FuncId, Thunk<Value>> child : tree.getChildren()) {
		Thunk<Value> thunk = child.getValue();
		try {
		    params.add(new Pair<Type, Value>(child.getType(), thunk.get()));
		} catch (InterruptedException ignored) {
		}
	    }
	    return params;
	}

	public void visit(NTree<Type, FuncId, Thunk<Value>> tree) throws VisitException {
	    try {
		if (ThunkUtils.isEvaluable(tree)) {
		    Pair<Type, Thunk<Value>> pair = factory.makeThunk(tree.getLabel(), extractThunkValues(tree));
		    tree.setLeaf(pair.getFirst(), pair.getSecond());
		} else if (tree.isLeaf() && !tree.getValue().isDone()) {
		    tree.getValue().step();
		}
	    } catch (ExecutionException e) {
		throw new VisitException(e);
	    } catch (UnknownFuncException e) {
		throw new VisitException(e);
	    }
	}
    }

}