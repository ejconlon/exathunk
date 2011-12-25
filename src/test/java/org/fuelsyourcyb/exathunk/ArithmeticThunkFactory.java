package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ArithmeticThunkFactory<State> implements ThunkFactory<String, Deque<String>, State, String, Integer> {

    private final TypeFactory<State> stateFactory;

    public ArithmeticThunkFactory(TypeFactory<State> stateFactory) {
	this.stateFactory = stateFactory;
    }

    public static class AddFunc2 implements Func2<Integer, Integer, Integer> {
	public Integer runFunc(Integer a, Integer b) { return a + b; }
    }

    public static class SubFunc2 implements Func2<Integer, Integer, Integer> {
	public Integer runFunc(Integer a, Integer b) { return a - b; }
    }

    public static class MulFunc2 implements Func2<Integer, Integer, Integer> {
	public Integer runFunc(Integer a, Integer b) { return a * b; }
    }

    public static class DivFunc2 implements Func2<Integer, Integer, Integer> {
	public Integer runFunc(Integer a, Integer b) { return a / b; }
    }

    public static class ModFunc2 implements Func2<Integer, Integer, Integer> {
	public Integer runFunc(Integer a, Integer b) { return a % b; }
    }

    private static final Map<String, Func2<Integer, Integer, Integer>> OPS = makeOps();

    private static Map<String, Func2<Integer, Integer, Integer>> makeOps() {
	Map<String, Func2<Integer, Integer, Integer>> ops = new TreeMap<String, Func2<Integer, Integer, Integer>>();
	ops.put("+", new AddFunc2());
	ops.put("-", new SubFunc2());
	ops.put("*", new MulFunc2());
	ops.put("/", new DivFunc2());
	ops.put("%", new ModFunc2());
	return ops;
    }

    public NTree<String, Thunk<State, Integer>> makeIntegerThunk(Integer n) {
	return new NTree<String, Thunk<State, Integer>>(
            new PresentThunk<State, Integer>(stateFactory.makeInstance(), n));
    }

    public NTree<String, Thunk<State, Integer>> createThunk(String funcId, Deque<String> params) {
	if (!OPS.containsKey(funcId)) {
	    throw new ThunkEvaluationException("Invalid op: "+funcId);
	} else {
	    List<NTree<String, Thunk<State, Integer>>> children = new ArrayList<NTree<String, Thunk<State, Integer>>>(2);
	    for (int i = 0; i < 2; ++i) {
		if (params.isEmpty()) {
		    throw new ThunkEvaluationException("Empty params");
		}
		String nextFuncId = params.removeFirst();
		if (OPS.containsKey(nextFuncId)) {
		    children.add(createThunk(nextFuncId, params));
		} else {
		    children.add(makeIntegerThunk(Integer.parseInt(nextFuncId)));
		}
	    }
	    return new NTree<String, Thunk<State, Integer>>(funcId, children);
	}
    }

    private class Evaluator implements ParametricMutator<Either<String, Thunk<State, Integer>>,
	NTree<String, Thunk<State, Integer>>> {
	public void mutate(Either<String, Thunk<State, Integer>> labelOrValue, NTree<String, Thunk<State, Integer>> tree) {
	    if (labelOrValue.isLeft()) {
		Thunk<State, Integer> t1 = tree.getChildren().get(0).getValue();
		Thunk<State, Integer> t2 = tree.getChildren().get(1).getValue();
		if (t1.isFinished()) {
		    if (t2.isFinished()) {
			Func2<Integer, Integer, Integer> f = OPS.get(labelOrValue.left());
			Integer result = f.runFunc(t1.getResult(), t2.getResult());
			tree.setValue(new PresentThunk<State, Integer>(stateFactory.makeInstance(), result));
		    } else {
			t2.step();
		    }
		} else {
		    t1.step();
		    if (!t2.isFinished()) {
			t2.step();
		    }
		}
	    }
	}
    }

    public ParametricMutator<Either<String, Thunk<State, Integer>>,
	NTree<String, Thunk<State, Integer>>> getEvaluator() {
	return new Evaluator();
    }

    public NTree<String, Thunk<State, Integer>> parse(String expression) {
	Scanner scanner = new Scanner(expression);
	scanner.useDelimiter(Pattern.compile(" "));
	Deque<String> tokens = new ArrayDeque<String>();
	for (; scanner.hasNext(); ) {
	    tokens.add(scanner.next());
	}
	if (tokens.isEmpty()) {
	    throw new ThunkEvaluationException("Empty string");
	} else {
	    return createThunk(tokens.removeFirst(), tokens);
	}
    }

}
					                    