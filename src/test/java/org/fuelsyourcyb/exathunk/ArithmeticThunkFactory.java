package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;
import java.util.TreeMap;

public class ArithmeticThunkFactory implements ThunkFactory<String, Deque<String>, Unit, String, Integer> {

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

    private NTree<String, Thunk<Unit, Integer>> makeIntegerThunk(Integer n) {
	return new NTree<String, Thunk<Unit, Integer>>(
            new PresentThunk<Unit, Integer>(Unit.getInstance(), n));
    }

    public NTree<String, Thunk<Unit, Integer>> createThunk(String funcId, Deque<String> params) {
	if (!OPS.containsKey(funcId)) {
	    throw new ThunkEvaluationException("Invalid op: "+funcId);
	} else {
	    List<NTree<String, Thunk<Unit, Integer>>> children = new ArrayList<NTree<String, Thunk<Unit, Integer>>>(2);
	    for (int i = 0; i < 2; ++i) {
		if (params.isEmpty()) {
		    throw new ThunkEvaluationException("Empty params");
		}
		String nextFuncId = params.removeFirst();
		if (OPS.containsKey(nextFuncId)) {
		    children.set(i, createThunk(nextFuncId, params));
		} else {
		    children.set(i, makeIntegerThunk(Integer.parseInt(nextFuncId)));
		}
	    }
	    return new NTree<String, Thunk<Unit, Integer>>(funcId, children);
	}
    }

    public ParametricMutator<Thunk<Unit, Integer>,
	NTree<String, Thunk<Unit, Integer>>> getEvaluator() {
	return null;
    }

}
					                    