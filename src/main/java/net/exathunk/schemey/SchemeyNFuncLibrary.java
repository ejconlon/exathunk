package net.exathunk.schemey;

import net.exathunk.functional.Either;

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
        public Thunk<VarCont> invoke(final NFuncLibrary library, final ThunkExecutor executor, 
                                     final Bindings bindings,
                                     final NTree<VarContType, FuncId, VarCont> args) {
            return new CallableThunk<>(new Callable<VarCont>() {
                @Override
                public VarCont call() throws ExecutionException, UnknownFuncException {
                    List<Boolean> mask1 = Arrays.asList(true, false);
                    Thunk<NTree<VarContType, FuncId, VarCont>> firstEvaled =
                            TreeExecutor.execute(executor, bindings, args, mask1);
                    if (!firstEvaled.get().getChildren().get(0).getValue().getSingletonCont().isBoolVar()) {
                        return VarUtils.makeBoolVarCont(false);
                    }
                    List<Boolean> mask2 = Arrays.asList(false, true);
                    Thunk<NTree<VarContType, FuncId, VarCont>> secondEvaled =
                            TreeExecutor.execute(executor, bindings, firstEvaled.get(), mask2);
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
        public Thunk<VarCont> invoke(final NFuncLibrary library, final ThunkExecutor executor,
                                     final Bindings bindings,
                                     final NTree<VarContType, FuncId, VarCont> args) {
            return new CallableThunk<>(new Callable<VarCont>() {
                @Override
                public VarCont call() throws ExecutionException, UnknownFuncException {
                    List<Boolean> mask1 = Arrays.asList(true, false);
                    Thunk<NTree<VarContType, FuncId, VarCont>> firstEvaled =
                            TreeExecutor.execute(executor, bindings, args, mask1);
                    if (firstEvaled.get().getChildren().get(0).getValue().getSingletonCont().isBoolVar()) {
                        return VarUtils.makeBoolVarCont(true);
                    }
                    List<Boolean> mask2 = Arrays.asList(false, true);
                    Thunk<NTree<VarContType, FuncId, VarCont>> secondEvaled =
                            TreeExecutor.execute(executor, bindings, firstEvaled.get(), mask2);
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

    public static class BottomFunc extends StrictNFuncImpl {
        public BottomFunc() { super(makeFuncDef()); }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeTemplateVarContType("BOTTOM_A"));
            def.setParameterTypes(new LinkedList<VarContType>());
            return def;
        }

        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            throw new UserExecutionException("Hit bottom");
        }
    }

    public static class ErrorFunc extends StrictNFuncImpl {
        public ErrorFunc() { super(makeFuncDef()); }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeTemplateVarContType("ERROR_A"));
            def.addToParameterTypes(VarUtils.makeStringVarContType());
            return def;
        }

        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            throw new UserExecutionException(values.get(0).getSingletonCont().getStringVar());
        }
    }

    public static class IfFunc extends NFuncImpl {
        public IfFunc() {
            super(makeFuncDef());
        }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeTemplateVarContType("IF_A"));
            def.addToParameterTypes(VarUtils.makeBoolVarContType());
            def.addToParameterTypes(VarUtils.makeTemplateVarContType("IF_A"));
            def.addToParameterTypes(VarUtils.makeTemplateVarContType("IF_A"));
            return def;
        }

        @Override
        public Thunk<VarCont> invoke(final NFuncLibrary library, final ThunkExecutor executor,
                                     final Bindings bindings,
                                     final NTree<VarContType, FuncId, VarCont> args) {
            return new CallableThunk<>(new Callable<VarCont>() {
                @Override
                public VarCont call() throws ExecutionException, UnknownFuncException {
                    List<Boolean> mask1 = Arrays.asList(true, false, false);
                    Thunk<NTree<VarContType, FuncId, VarCont>> firstEvaled =
                            TreeExecutor.execute(executor, bindings, args, mask1);
                    int exdex;
                    if (firstEvaled.get().getChildren().get(0).getValue().getSingletonCont().isBoolVar()) {
                        exdex = 1;
                    } else {
                        exdex = 2;
                    }
                    List<Boolean> mask2 = Arrays.asList(false, 1 == exdex, 2 == exdex);
                    Thunk<NTree<VarContType, FuncId, VarCont>> secondEvaled =
                            TreeExecutor.execute(executor, bindings, firstEvaled.get(), mask2);
                    return secondEvaled.get().getChildren().get(exdex).getValue();
                }
            });
        }
    }

    public static class EqFunc extends StrictNFuncImpl {
        public EqFunc() { super(makeFuncDef()); }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeBoolVarContType());
            def.addToParameterTypes(VarUtils.makeTemplateVarContType("EQ_A"));
            def.addToParameterTypes(VarUtils.makeTemplateVarContType("EQ_A"));
            return def;
        }

        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeBoolVarCont(values.get(0).equals(values.get(1)));
        }
    }

    public static class NeqFunc extends StrictNFuncImpl {
        public NeqFunc() { super(EqFunc.makeFuncDef()); }

        @Override
        protected VarCont subInvoke(List<VarCont> values) throws ExecutionException {
            return VarUtils.makeBoolVarCont(!values.get(0).equals(values.get(1)));
        }
    }


    public static class LetFunc extends NFuncImpl {
        public LetFunc() {
            super(makeFuncDef());
        }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeTemplateVarContType("LET_B"));
            def.addToParameterTypes(VarUtils.makeStringVarContType());
            def.addToParameterTypes(VarUtils.makeTemplateVarContType("LET_A"));
	    def.addToParameterTypes(VarUtils.makeTemplateVarContType("LET_B"));
            return def;
        }

        @Override
        public Thunk<VarCont> invoke(final NFuncLibrary library, final ThunkExecutor executor,
                                     final Bindings bindings,
                                     final NTree<VarContType, FuncId, VarCont> args) {
            return new CallableThunk<>(new Callable<VarCont>() {
                @Override
                public VarCont call() throws ExecutionException, UnknownFuncException {
                    List<Boolean> mask1 = Arrays.asList(true, false, false);
                    Thunk<NTree<VarContType, FuncId, VarCont>> firstEvaled =
                            TreeExecutor.execute(executor, bindings, args, mask1);
		    String name = firstEvaled.get().getChildren().get(0).getValue().getSingletonCont().getStringVar();
		    NTree<VarContType, FuncId, VarCont> target = args.getChildren().get(1);
		    if (target.isLeaf()) {
			bindings.setEvaled(name, new Pair<>(target.getType(), target.getValue()));
		    } else {
			bindings.setUnevaled(name, target);
		    }
		    List<Boolean> mask2 = Arrays.asList(false, false, true);
                    Thunk<NTree<VarContType, FuncId, VarCont>> secondEvaled =
			TreeExecutor.execute(executor, bindings, args, mask2);
                    return secondEvaled.get().getChildren().get(1).getValue();
                }
            });
        }
    }

    public static class GetFunc extends NFuncImpl {
        public GetFunc() { super(makeFuncDef()); }

        public static FuncDef makeFuncDef() {
            FuncDef def = new FuncDef();
            def.setReturnType(VarUtils.makeTemplateVarContType("GET_A"));
            def.addToParameterTypes(VarUtils.makeStringVarContType());
            return def;
        }

        @Override
        public Thunk<VarCont> invoke(final NFuncLibrary library, final ThunkExecutor executor,
                                     final Bindings bindings,
                                     final NTree<VarContType, FuncId, VarCont> args) {
            return new CallableThunk<>(new Callable<VarCont>() {
                @Override
                public VarCont call() throws ExecutionException, UnknownFuncException {
                    List<Boolean> mask1 = Arrays.asList(true);
                    Thunk<NTree<VarContType, FuncId, VarCont>> firstEvaled =
                            TreeExecutor.execute(executor, bindings, args, mask1);
		    String id = firstEvaled.get().getChildren().get(0).getValue().getSingletonCont().getStringVar();
		    
		    Either<Pair<Bindings, NTree<VarContType, FuncId, VarCont>>, Pair<VarContType, VarCont>> eitherVal = bindings.getShallowest(id);
		    if (eitherVal == null) {
			throw new UserExecutionException("Cannot find value for "+id);
		    } else if (eitherVal.isLeft()) {
			Bindings realBindings = eitherVal.left().getFirst();
			NTree<VarContType, FuncId, VarCont> tree = eitherVal.left().getSecond();
			if (!TypeCheckerUtils.typeEquals(tree.getType(), args.getType()))
			    throw new UserExecutionException("Invalid types: "+tree.getType()+" "+args.getType());
			Thunk<NTree<VarContType, FuncId, VarCont>> secondEvaled =
                            TreeExecutor.execute(executor, bindings, tree, mask1);
			VarCont val = secondEvaled.get().getValue();
			realBindings.setEvaled(id, new Pair<>(tree.getType(), val));
			return val;
		    } else {
			VarContType type = eitherVal.right().getFirst();
			if (!TypeCheckerUtils.typeEquals(type, args.getType()))
			    throw new UserExecutionException("Invalid types: "+type+" "+args.getType());
			return eitherVal.right().getSecond();
		    }
                }
            });
        }
    }

    // ************************************************************************

    private static void put(Map<String, NFunc> funcs, String name, NFunc func) {
        func.getFuncDef().setFuncId(new FuncId(name));
        funcs.put(name, func);
    }
    
    private static Map<String, NFunc> makeFuncs() {
        Map<String, NFunc> funcs = new TreeMap<>();
        put(funcs, "+", new AddFunc());
        put(funcs, "-", new SubFunc());
        put(funcs, "*", new MulFunc());
        put(funcs, "/", new DivFunc());
        put(funcs, "%", new ModFunc());
        put(funcs, "and", new AndFunc());
        put(funcs, "or", new OrFunc());
        put(funcs, "xor", new XorFunc());
        put(funcs, "not", new NotFunc());
        put(funcs, "len", new LenFunc());
        put(funcs, "bottom", new BottomFunc());
        put(funcs, "error", new ErrorFunc());
        put(funcs, "if", new IfFunc());
        put(funcs, "eq", new EqFunc());
        put(funcs, "neq", new NeqFunc());
	put(funcs, "let", new LetFunc());
	put(funcs, "get", new GetFunc());
        return funcs;
    }
}
					                    