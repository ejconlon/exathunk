package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class ArithmeticThunkFactory implements ThunkFactory<Class, String, Object> {

    private static final Map<String, NFunc<Class, Object>> FUNCS = makeFuncs();

    public boolean knowsFunc(String funcId) {
	return FUNCS.containsKey(funcId);
    }

    protected NFunc<Class, Object> getFunc(String funcId) throws UnknownFuncException {
	NFunc<Class, Object> f = FUNCS.get(funcId);
	if (f == null) throw new UnknownFuncException("Unknown func: "+funcId);
	return f;
    }

    public List<Class> getParameterTypes(String funcId) throws UnknownFuncException {
	return getFunc(funcId).getParameterTypes();
    }

    public Class getReturnType(String funcId) throws UnknownFuncException {
	return getFunc(funcId).getReturnType();
    }

    public Thunk<Object> makeThunk(String funcId, List<Object> params) throws UnknownFuncException, ExecutionException {
	return new PresentThunk<Object>(getFunc(funcId).invoke(params));
    }

    private static abstract class IntFunc extends NFuncImpl<Class, Object> {
	public IntFunc() {
	    super(Integer.class, new Class[] { Integer.class, Integer.class });
	}

	public Object invoke(List<Object> args) {
	    return subInvoke((Integer)args.get(0),
 		             (Integer)args.get(1));
	}

	protected abstract Integer subInvoke(Integer a, Integer b);
    }

    public static class AddFunc extends IntFunc {
	protected Integer subInvoke(Integer a, Integer b) { return a + b; }
    }

    public static class SubFunc extends IntFunc {
	protected Integer subInvoke(Integer a, Integer b) { return a - b; }
    }

    public static class MulFunc extends IntFunc {
	protected Integer subInvoke(Integer a, Integer b) { return a * b; }
    }

    public static class DivFunc extends IntFunc {
	protected Integer subInvoke(Integer a, Integer b) { return a / b; }
    }

    public static class ModFunc extends IntFunc {
	protected Integer subInvoke(Integer a, Integer b) { return a % b; }
    }

    private static abstract class BoolFunc2 extends NFuncImpl<Class, Object> {
	public BoolFunc2() {
	    super(Boolean.class, new Class[] { Boolean.class, Boolean.class });
	}

	public Object invoke(List<Object> args) {
	    return subInvoke((Boolean)args.get(0),
 		             (Boolean)args.get(1));
	}

	protected abstract Boolean subInvoke(Boolean a, Boolean b);
    }

    public static class AndFunc extends BoolFunc2 {
	protected Boolean subInvoke(Boolean a, Boolean b) { return a && b; }
    }

    public static class OrFunc extends BoolFunc2 {
	protected Boolean subInvoke(Boolean a, Boolean b) { return a || b; }
    }

    public static class XorFunc extends BoolFunc2 {
	protected Boolean subInvoke(Boolean a, Boolean b) { return a ^ b; }
    }

    private static abstract class BoolFunc1 extends NFuncImpl<Class, Object> {
	public BoolFunc1() {
	    super(Boolean.class, new Class[] { Boolean.class });
	}

	public Object invoke(List<Object> args) {
	    return subInvoke((Boolean)args.get(0));
	}

	protected abstract Boolean subInvoke(Boolean a);
    }

    public static class NotFunc extends BoolFunc1 {
	protected Boolean subInvoke(Boolean a) { return !a; }
    }

    public static class LenFunc extends NFunc1<Class, Object> {
	public LenFunc() { super(Integer.class, String.class); }

	public Object invoke(List<Object> args) {
	    return ((String)args.get(0)).length();
	}
    }

    private static Map<String, NFunc<Class, Object>> makeFuncs() {
	Map<String, NFunc<Class, Object>> funcs = new TreeMap<>();
	funcs.put("+", new AddFunc());
	funcs.put("-", new SubFunc());
	funcs.put("*", new MulFunc());
	funcs.put("/", new DivFunc());
	funcs.put("%", new ModFunc());
	funcs.put("and", new AndFunc());
	funcs.put("or", new OrFunc());
	funcs.put("xor", new XorFunc());
	funcs.put("not", new NotFunc());
	funcs.put("len", new LenFunc());
	return funcs;
    }
}
					                    