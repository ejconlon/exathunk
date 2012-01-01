package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ArithmeticThunkFactory implements ThunkFactory<Class, String, Object> {

    private final Map<String, NFunc<Class, Object>> funcs = makeFuncs();

    public boolean knowsFunc(String funcId) {
        return funcs.containsKey(funcId);
    }

    protected NFunc<Class, Object> getFunc(String funcId) throws UnknownFuncException {
        NFunc<Class, Object> f = funcs.get(funcId);
        if (f == null) throw new UnknownFuncException("Unknown func: "+funcId);
        return f;
    }

    public List<Class> getParameterTypes(String funcId) throws UnknownFuncException {
        return getFunc(funcId).getParameterTypes();
    }

    public Class getReturnType(String funcId) throws UnknownFuncException {
        return getFunc(funcId).getReturnType();
    }

    public Thunk<Object> makeThunk(String funcId, List<Object> params) throws UnknownFuncException {
        return getFunc(funcId).invoke(params);
    }

    private static abstract class IntFunc extends NFuncImpl<Class, Object> {
        public IntFunc() {
            super(Integer.class, new Class[] { Integer.class, Integer.class });
        }
    }

    public static class AddFunc extends IntFunc {
        protected Object subInvoke(final List<Object> a) {
            return (Integer)a.get(0) + (Integer)a.get(1);
        }
    }

    public static class SubFunc extends IntFunc {
        protected Object subInvoke(final List<Object> a) {
            return (Integer)a.get(0) - (Integer)a.get(1);
        }
    }

    public static class MulFunc extends IntFunc {
        protected Object subInvoke(final List<Object> a) {
            return (Integer)a.get(0) * (Integer)a.get(1);
        }
    }

    public static class DivFunc extends IntFunc {
        protected Object subInvoke(final List<Object> a) {
            return (Integer)a.get(0) / (Integer)a.get(1);
        }
    }

    public static class ModFunc extends IntFunc {
        protected Object subInvoke(final List<Object> a) {
            return (Integer)a.get(0) % (Integer)a.get(1);
        }
    }

    private static abstract class BoolFunc2 extends NFuncImpl<Class, Object> {
        public BoolFunc2() {
            super(Boolean.class, new Class[] { Boolean.class, Boolean.class });
        }
    }

    public static class AndFunc extends BoolFunc2 {
        protected Object subInvoke(final List<Object> a) {
            return (Boolean)a.get(0) && (Boolean)a.get(1);
        }
    }

    public static class OrFunc extends BoolFunc2 {
        protected Object subInvoke(final List<Object> a) {
            return (Boolean)a.get(0) || (Boolean)a.get(1);
        }
    }

    public static class XorFunc extends BoolFunc2 {
        protected Object subInvoke(final List<Object> a) {
            return (Boolean)a.get(0) ^ (Boolean)a.get(1);
        }
    }

    private static class NotFunc extends NFuncImpl<Class, Object> {
        public NotFunc() {
            super(Boolean.class, new Class[] { Boolean.class });
        }

        protected Object subInvoke(final List<Object> a) {
            return !(Boolean)a.get(0);
        }
    }

    public static class LenFunc extends NFuncImpl<Class, Object> {
        public LenFunc() { super(Integer.class, new Class[] { String.class }); }

        protected Object subInvoke(final List<Object> a) {
            return ((String)a.get(0)).length();
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
					                    