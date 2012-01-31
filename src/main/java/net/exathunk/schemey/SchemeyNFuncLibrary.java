package net.exathunk.schemey;

import net.exathunk.base.*;
import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class SchemeyNFuncLibrary implements NFuncLibrary {

    private final Map<String, NFunc> funcs = makeFuncs();

    public boolean knowsFunc(FuncId funcId) {
        return funcs.containsKey(funcId);
    }

    public FuncDef getFuncDef(FuncId funcId) throws UnknownFuncException {
        return getFunc(funcId).getFuncDef();
    }
    
    public List<FuncDef> getFuncDefs(List<FuncId> funcIds) throws UnknownFuncException {
        List<FuncDef> funcDefs = new ArrayList<>(funcIds.size());
        for (FuncId funcId : funcIds) funcDefs.add(getFuncDef(funcId));
        return funcDefs;
    }

    public NFunc getFunc(FuncId funcId) throws UnknownFuncException {
        NFunc f = funcs.get(funcId.getName());
        if (f == null) throw new UnknownFuncException("Unknown func: "+funcId);
        return f;
    }

    private static abstract class IntFunc extends StrictNFuncImpl {
        public IntFunc() {
            super(makeFuncDef());
        }
        
        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeLongVarContType());
            def.addToParameterTypes(VarUtils.makeLongVarContType());
            def.addToParameterTypes(VarUtils.makeLongVarContType());
            return def;
        }
    }

    public static class AddFunc extends IntFunc {
        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeLongVarCont(
                    values.get(0).getSingletonCont().getI64Var() +
                            values.get(1).getSingletonCont().getI64Var());
        }
    }

    public static class SubFunc extends IntFunc {
        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeLongVarCont(
                    values.get(0).getSingletonCont().getI64Var() -
                            values.get(1).getSingletonCont().getI64Var());
        }
    }

    public static class MulFunc extends IntFunc {
        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeLongVarCont(
                    values.get(0).getSingletonCont().getI64Var() *
                            values.get(1).getSingletonCont().getI64Var());
        }
    }

    public static class DivFunc extends IntFunc {
        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeLongVarCont(
                    values.get(0).getSingletonCont().getI64Var() /
                            values.get(1).getSingletonCont().getI64Var());
        }
    }

    public static class ModFunc extends IntFunc {
        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeLongVarCont(
                    values.get(0).getSingletonCont().getI64Var() %
                            values.get(1).getSingletonCont().getI64Var());
        }
    }

    private static abstract class BoolFunc2 extends NFuncImpl {
        public BoolFunc2() {
            super(makeFuncDef());
        }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeBoolVarContType());
            def.addToParameterTypes(VarUtils.makeBoolVarContType());
            def.addToParameterTypes(VarUtils.makeBoolVarContType());
            return def;
        }
    }

    public static class AndFunc extends BoolFunc2 {
        @Override
        public Thunk<VarCont> invoke(final NFuncLibrary library, final ThunkExecutor executor, final NTree<VarContType, FuncId, VarCont> args) {
            return new CallableThunk<>(new Callable<VarCont>() {
                @Override
                public VarCont call() throws ExecutionException, UnknownFuncException {
                    List<Boolean> mask1 = Arrays.asList(true, false);
                    Thunk<NTree<VarContType, FuncId, VarCont>> firstEvaled =
                            TreeExecutor.execute(executor, args, mask1);
                    if (!firstEvaled.get().getChildren().get(0).getValue().getSingletonCont().isBoolVar()) {
                        return VarUtils.makeBoolVarCont(false);
                    }
                    List<Boolean> mask2 = Arrays.asList(false, true);
                    Thunk<NTree<VarContType, FuncId, VarCont>> secondEvaled =
                            TreeExecutor.execute(executor, firstEvaled.get(), mask2);
                    if (!secondEvaled.get().getChildren().get(1).getValue().getSingletonCont().isBoolVar()) {
                        return VarUtils.makeBoolVarCont(false);
                    }
                    return VarUtils.makeBoolVarCont(true);
                }
            });
        }
    }

    public static class OrFunc extends BoolFunc2 {
        @Override
        public Thunk<VarCont> invoke(final NFuncLibrary library, final ThunkExecutor executor, final NTree<VarContType, FuncId, VarCont> args) {
            return new CallableThunk<>(new Callable<VarCont>() {
                @Override
                public VarCont call() throws ExecutionException, UnknownFuncException {
                    List<Boolean> mask1 = Arrays.asList(true, false);
                    Thunk<NTree<VarContType, FuncId, VarCont>> firstEvaled =
                            TreeExecutor.execute(executor, args, mask1);
                    if (firstEvaled.get().getChildren().get(0).getValue().getSingletonCont().isBoolVar()) {
                        return VarUtils.makeBoolVarCont(true);
                    }
                    List<Boolean> mask2 = Arrays.asList(false, true);
                    Thunk<NTree<VarContType, FuncId, VarCont>> secondEvaled =
                            TreeExecutor.execute(executor, firstEvaled.get(), mask2);
                    if (secondEvaled.get().getChildren().get(1).getValue().getSingletonCont().isBoolVar()) {
                        return VarUtils.makeBoolVarCont(true);
                    }
                    return VarUtils.makeBoolVarCont(false);
                }
            });
        }
    }

    private static abstract class StrictBoolFunc2 extends StrictNFuncImpl {
        public StrictBoolFunc2() {
            super(makeFuncDef());
        }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeBoolVarContType());
            def.addToParameterTypes(VarUtils.makeBoolVarContType());
            def.addToParameterTypes(VarUtils.makeBoolVarContType());
            return def;
        }
    }
    
    public static class XorFunc extends StrictBoolFunc2 {
        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeBoolVarCont(
                    values.get(0).getSingletonCont().isBoolVar() ^
                            values.get(1).getSingletonCont().isBoolVar());
        }
    }

    private static class NotFunc extends StrictNFuncImpl {
        public NotFunc() {
            super(makeFuncDef());
        }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeBoolVarContType());
            def.addToParameterTypes(VarUtils.makeBoolVarContType());
            return def;
        }

        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeBoolVarCont(
                    !values.get(0).getSingletonCont().isBoolVar());
        }
    }

    public static class LenFunc extends StrictNFuncImpl {
        public LenFunc() { super(makeFuncDef()); }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeLongVarContType());
            def.addToParameterTypes(VarUtils.makeStringVarContType());
            return def;
        }

        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeLongVarCont(
                    values.get(0).getSingletonCont().getStringVar().length());
        }
    }

    // TODO add parametric/wildcard types to support these
    /*public static class BottomFunc extends StrictNFuncImpl {

        public BottomFunc() {
            super(Any.class, new Class[] {});
        }


        @Override
        protected Object subInvoke(List<Object> objects) throws ExecutionException {
            throw new ThunkExecutionException("Hit bottom");
        }
    }
    
    public static class IfFunc extends NFuncImpl {
        public IfFunc() {
            super(Any.class, new Class[] { Boolean.class, Any.class, Any.class },
                    new Strictness[] {Strictness.STRICT, Strictness.LAZY, Strictness.LAZY});
        }

        @Override
        public Thunk<Object> invoke(final ThunkFactory<Class, String, Object> thunkFactory, final ThunkExecutor<Object> executor, final NTree<Class, String, Object> args) {
            return new CallableThunk<>(new Callable<Object>() {
                @Override
                public Object call() throws ExecutionException, UnknownFuncException {
                    List<Boolean> mask1 = Arrays.asList(true, false);
                    Thunk<NTree<Class, String, Object>> firstEvaled =
                            TreeExecutor.execute(thunkFactory, executor, args, mask1);
                    int exdex;
                    if (Boolean.TRUE.equals(args.getChildren().get(0).getValue())) {
                        exdex = 1;
                    } else {
                        exdex = 2;
                    }
                    List<Boolean> mask2 = Arrays.asList(false, 1 == exdex, 2 == exdex);
                    Thunk<NTree<Class, String, Object>> secondEvaled =
                            TreeExecutor.execute(thunkFactory, executor, firstEvaled.get(), mask2);
                    return secondEvaled.get().getChildren().get(exdex).getValue();
                }
            });
        }
    }*/

    private static Map<String, NFunc> makeFuncs() {
        Map<String, NFunc> funcs = new TreeMap<>();
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
        //funcs.put("bottom", new BottomFunc());
        //funcs.put("if", new IfFunc());
        return funcs;
    }
}
					                    