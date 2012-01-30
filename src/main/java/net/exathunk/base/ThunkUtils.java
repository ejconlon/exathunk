package net.exathunk.base;

import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

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
            } catch (ExecutionException e) {
                return false;
            }
        }
        return false;
    }

    public static <Type, FuncId, Value> boolean isEvaluable(NTree<Type, FuncId, Thunk<Value>> thunkTree) {
        if (thunkTree.isBranch()) {
            for (NTree<Type, FuncId, Thunk<Value>> child : thunkTree.getChildren()) {
                if (!child.isLeaf()) return false;
                if (!child.getValue().isDone()) return false;
            }
            return true;
        }
        return false;
    }

    public static <Type, FuncId, Value> NTree<Type, FuncId, Value> unthunkValues(NTree<Type, FuncId, Thunk<Value>> tree) throws ExecutionException, InterruptedException {
        List<NTree<Type, FuncId, Value>> unChildren = new ArrayList<>(tree.getChildren().size());
        for (NTree<Type, FuncId, Thunk<Value>> child : tree.getChildren()) {
            Thunk<Value> thunk = child.getValue();
            assert thunk.isDone();
            unChildren.add(new NTree<Type, FuncId, Value>(child.getType(), thunk.get()));
        }
        return new NTree<>(tree.getType(), tree.getLabel(), unChildren);
    }

    public static Thunk<VarCont> makeThunk(NFuncLibrary library, ThunkExecutor<VarCont> executor,
                             NTree<VarContType, FuncId, VarCont> tree) throws UnknownFuncException, ExecutionException {
        NFunc func = library.getFunc(tree.getLabel());
        return func.invoke(library, executor, tree);
    }
}