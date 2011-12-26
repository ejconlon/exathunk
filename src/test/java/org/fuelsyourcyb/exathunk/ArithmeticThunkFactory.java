package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// Example implementation of a ThunkFactory.  Parses and evaluates RPN expressions like "* 5 / 12 3" (== 20).
// Arithmetic operations like +, *, etc function as local combinators on thunked int literals.  Subclassing
// implementations can choose to defer evaluation of these literals.
public class ArithmeticThunkFactory implements ThunkFactory<String, Integer> {

    private static final Map<String, Func2<Integer, Integer, Integer>> OPS = makeOps();

    private final StateFactory<State> stateFactory;

    public ArithmeticThunkFactory(StateFactory<State> stateFactory) {
	this.stateFactory = stateFactory;
    }

    public boolean knowsFunc(String funcId) {
	return OPS.containsKey(funcId);
    }

    public Thunk<Integer> makeThunk(String funcId, List<Integer> params) {
	Func2<Integer, Integer, Integer> op = OPS.get(funcId);
	Integer a = params.get(0);
	Integer b = params.get(1);
	Integer c = op.runFunc(a, b);
	return new PresentThunk<Integer>(stateFactory.makeInitialState(), c);
    }

    public StateFactory<State> getStateFactory() {
	return stateFactory;
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

    private static Map<String, Func2<Integer, Integer, Integer>> makeOps() {
	Map<String, Func2<Integer, Integer, Integer>> ops = new TreeMap<String, Func2<Integer, Integer, Integer>>();
	ops.put("+", new AddFunc2());
	ops.put("-", new SubFunc2());
	ops.put("*", new MulFunc2());
	ops.put("/", new DivFunc2());
	ops.put("%", new ModFunc2());
	return ops;
    }

}
					                    