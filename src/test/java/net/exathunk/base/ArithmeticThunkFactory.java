package net.exathunk.base;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ArithmeticThunkFactory implements ThunkFactory<Class, String, Object> {

    private final Map<String, NFunc<Class, String, Object>> funcs = makeFuncs();

    public boolean knowsFunc(String funcId) {
        return funcs.containsKey(funcId);
    }

    public List<Strictness> getStrictnesses(String funcId) throws UnknownFuncException {
        return getFunc(funcId).getStrictnesses();
    }

    protected NFunc<Class, String, Object> getFunc(String funcId) throws UnknownFuncException {
        NFunc<Class, String, Object> f = funcs.get(funcId);
        if (f == null) throw new UnknownFuncException("Unknown func: "+funcId);
        return f;
    }

    public List<Class> getParameterTypes(String funcId) throws UnknownFuncException {
        return getFunc(funcId).getParameterTypes();
    }

    public Class getReturnType(String funcId) throws UnknownFuncException {
        return getFunc(funcId).getReturnType();
    }

    public Thunk<Object> makeThunk(ThunkExecutor<Object> executor, NTree<Class, String, Object> tree) throws UnknownFuncException {
        return getFunc(tree.getLabel()).invoke(this, executor, tree);
    }

    private static abstract class IntFunc extends StrictNFuncImpl<Class, String, Object> {
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

    private static abstract class StrictBoolFunc2 extends StrictNFuncImpl<Class, String, Object> {
        public StrictBoolFunc2() {
            super(Boolean.class, new Class[] { Boolean.class, Boolean.class });
        }
    }

    private static abstract class LazyBoolFunc2 extends NFuncImpl<Class, String, Object> {
        public LazyBoolFunc2() {
            super(Boolean.class, new Class[] { Boolean.class, Boolean.class },
                    new Strictness[] {Strictness.STRICT, Strictness.LAZY});
        }
    }

    public static class AndFunc extends LazyBoolFunc2 {
        protected Object subInvoke(final List<Object> a) {
            return (Boolean)a.get(0) && (Boolean)a.get(1);
        }

        @Override
        public Thunk<Object> invoke(final ThunkFactory<Class, String, Object> thunkFactory, 
                                    final ThunkExecutor<Object> executor, final NTree<Class, String, Object> args) {
            return new CallableThunk<>(new Callable<Object>() {
                @Override
                public Object call() throws ExecutionException, UnknownFuncException {
                    ThunkUtils.execute(thunkFactory, executor, args.getChildren().get(0));
                    if (Boolean.FALSE.equals(args.getChildren().get(0).getValue())) {
                        return Boolean.FALSE;
                    }
                    ThunkUtils.execute(thunkFactory, executor, args.getChildren().get(1));
                    if (Boolean.FALSE.equals(args.getChildren().get(1).getValue())) {
                        return Boolean.FALSE;
                    }
                    return Boolean.TRUE;
                }
            });
        }
    }

    public static class OrFunc extends LazyBoolFunc2 {
        protected Object subInvoke(final List<Object> a) {
            return (Boolean)a.get(0) || (Boolean)a.get(1);
        }

        @Override
        public Thunk<Object> invoke(final ThunkFactory<Class, String, Object> thunkFactory,
                                    final ThunkExecutor<Object> executor, final NTree<Class, String, Object> args) {
            return new CallableThunk<>(new Callable<Object>() {
                @Override
                public Object call() throws ExecutionException, UnknownFuncException {
                    ThunkUtils.execute(thunkFactory, executor, args.getChildren().get(0));
                    if (Boolean.TRUE.equals(args.getChildren().get(0).getValue())) {
                        return Boolean.TRUE;
                    }
                    ThunkUtils.execute(thunkFactory, executor, args.getChildren().get(1));
                    if (Boolean.TRUE.equals(args.getChildren().get(1).getValue())) {
                        return Boolean.TRUE;
                    }
                    return Boolean.FALSE;
                }
            });
        }
    }

    public static class XorFunc extends StrictBoolFunc2 {
        protected Object subInvoke(final List<Object> a) {
            return (Boolean)a.get(0) ^ (Boolean)a.get(1);
        }
    }

    private static class NotFunc extends StrictNFuncImpl<Class, String, Object> {
        public NotFunc() {
            super(Boolean.class, new Class[] { Boolean.class });
        }

        protected Object subInvoke(final List<Object> a) {
            return !(Boolean)a.get(0);
        }
    }

    public static class LenFunc extends StrictNFuncImpl<Class, String, Object> {
        public LenFunc() { super(Integer.class, new Class[] { String.class }); }

        protected Object subInvoke(final List<Object> a) {
            return ((String)a.get(0)).length();
        }
    }
    
    public static class BottomFunc extends StrictNFuncImpl<Class, String, Object> {

        public BottomFunc() {
            super(Any.class, new Class[] {});
        }


        @Override
        protected Object subInvoke(List<Object> objects) throws ExecutionException {
            throw new ThunkExecutionException("Hit bottom");
        }
    }

    private static Map<String, NFunc<Class, String, Object>> makeFuncs() {
        Map<String, NFunc<Class, String, Object>> funcs = new TreeMap<>();
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
        funcs.put("bottom", new BottomFunc());
        return funcs;
    }
}
					                    