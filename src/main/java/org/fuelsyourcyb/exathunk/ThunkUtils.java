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

    public static <FuncId, Value> boolean isTerminalInnerNode(NTree<FuncId, Value> parseTree) {
	if (parseTree.isBranch()) {
	    for (NTree<FuncId, Value> child : parseTree.getChildren()) {
		if (!child.isLeaf()) return false;
	    }
	    return true;
	}
	return false;
    }

    public static <FuncId, Value> boolean isEvaluatable(NTree<FuncId, Thunk<Value>> thunkTree) {
	if (thunkTree.isBranch()) {
	    for (NTree<FuncId, Thunk<Value>> child : thunkTree.getChildren()) {
		if (!child.isLeaf()) return false;
		if (!child.getValue().isDone()) return false;
	    }
	    return true;
	}
	return false;
    }

    public static <FuncId, Value> NTree<FuncId, Thunk<Value>> makeThunkTree(
            NTree<FuncId, Value> parseTree,
	    ThunkFactory<FuncId, Value> thunkFactory) throws ThunkEvaluationException {
	if (parseTree.isEmpty()) {
	    throw new ThunkEvaluationException("Empty computation");
	} else if (parseTree.isLeaf()) {
	    State initState = thunkFactory.getStateFactory().makeInitialState();
	    return new NTree<FuncId, Thunk<Value>>(new PresentThunk<Value>(initState, parseTree.getValue()));
	} else if (ThunkUtils.isTerminalInnerNode(parseTree)) {
	    List<Value> params = new ArrayList<Value>(parseTree.getChildren().size());
	    for (NTree<FuncId, Value> child : parseTree.getChildren()) {
		params.add(child.getValue());
	    }
	    return new NTree<FuncId, Thunk<Value>>(thunkFactory.makeThunk(parseTree.getLabel(), params));
	} else {
	    List<NTree<FuncId, Thunk<Value>>> params = new ArrayList<NTree<FuncId, Thunk<Value>>>(parseTree.getChildren().size());
	    for (NTree<FuncId, Value> child : parseTree.getChildren()) {
		params.add(makeThunkTree(child, thunkFactory));
	    }
	    return new NTree<FuncId, Thunk<Value>>(parseTree.getLabel(), params);
	}
    }

    public static interface Evaluator<FuncId, Value> extends
        ParametricMutator<Either<FuncId, Thunk<Value>>, NTree<FuncId, Thunk<Value>>> {}

    public static class StepEvaluator<FuncId, Value> implements Evaluator<FuncId, Value> {
	private final ThunkFactory<FuncId, Value> factory;

	public StepEvaluator(ThunkFactory<FuncId, Value> factory) {
	    this.factory = factory;
	}

	public void mutate(Either<FuncId, Thunk<Value>> param, NTree<FuncId, Thunk<Value>> mutee) {
	    if (param.isLeft()) {
		if (ThunkUtils.isEvaluatable(mutee)) {
		    List<Value> params = new ArrayList<Value>(mutee.getChildren().size());
		    for (NTree<FuncId, Thunk<Value>> child : mutee.getChildren()) {
			Thunk<Value> thunk = child.getValue();
			try {
			    params.add(thunk.get());
			} catch (InterruptedException e) {
			    return;  // TODO(ejconlon) return or throw an exception
			} catch (ExecutionException e) {
			    return;
			}

		    }
		    mutee.setValue(factory.makeThunk(param.left(), params));
		} 
	    } else if (!mutee.getValue().isDone()) {
		mutee.getValue().step();
	    }
	}
    }

}