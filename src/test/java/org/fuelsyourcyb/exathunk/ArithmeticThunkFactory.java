package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ArithmeticThunkFactory implements ThunkFactory<Class, String, Integer> {

    private static final Map<String, Func2<Integer, Integer, Integer>> FUNCS = makeFuncs();
    private static final Map<String, List<Class>> TYPES = makeTypes();

    private final StateFactory<State> stateFactory;

    public ArithmeticThunkFactory(StateFactory<State> stateFactory) {
	this.stateFactory = stateFactory;
    }

    public boolean knowsFunc(String funcId) {
	return TYPES.containsKey(funcId);
    }

    public List<Class> getTypeSpec(String funcId) throws UnknownFuncException{
	List<Class> l = TYPES.get(funcId);
	if (l == null) {
	    throw new UnknownFuncException("Unknown func: "+funcId);
	} else {
	    return l;
	}
    }

    public Pair<Class, Thunk<Integer>> makeThunk(String funcId, List<Pair<Class, Integer>> params) throws UnknownFuncException {
	Func2<Integer, Integer, Integer> op = FUNCS.get(funcId);
	if (op == null) {
	    throw new UnknownFuncException("Unknown func: "+funcId);
	} else {
	    Integer a = params.get(0).getSecond();
	    Integer b = params.get(1).getSecond();
	    Integer c = op.runFunc(a, b);
	    return new Pair<Class, Thunk<Integer>>(Integer.class,
						   new PresentThunk<Integer>(stateFactory.makeInitialState(), c));
	}
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

    private static Map<String, Func2<Integer, Integer, Integer>> makeFuncs() {
	Map<String, Func2<Integer, Integer, Integer>> funcs = new TreeMap<>();
	funcs.put("+", new AddFunc2());
	funcs.put("-", new SubFunc2());
	funcs.put("*", new MulFunc2());
	funcs.put("/", new DivFunc2());
	funcs.put("%", new ModFunc2());
	return funcs;
    }

    private static Map<String, List<Class>> makeTypes() {
	Map<String, List<Class>> types = new TreeMap<>();
	List<Class> typeSpec = new ArrayList<Class>(3);
	typeSpec.add(Integer.class);
	typeSpec.add(Integer.class);
	typeSpec.add(Integer.class);
	typeSpec = Collections.unmodifiableList(typeSpec);
	types.put("+", typeSpec);
	types.put("-", typeSpec);
	types.put("*", typeSpec);
	types.put("/", typeSpec);
	types.put("%", typeSpec);
	return types;
    }

}
					                    