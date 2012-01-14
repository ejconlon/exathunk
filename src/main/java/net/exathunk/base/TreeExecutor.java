package net.exathunk.base;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class TreeExecutor {
    public static <Type, FuncId, Value> Thunk<NTree<Type, FuncId, Value>> execute(
            ThunkFactory<Type, FuncId, Value> thunkFactory,
            ThunkExecutor<Value> executor,
            NTree<Type, FuncId, Value> tree,
            List<Boolean> executeMask) throws UnknownFuncException, ExecutionException {
        if (tree.isEmpty()) {
            throw new ThunkExecutionException("Cannot execute empty tree");
        } else if (tree.isLeaf()) {
            return new PresentThunk<>(tree);
        } else {
            Map<Integer, NTree<Type, FuncId, Value>> unevaluated = new TreeMap<>();
            Map<Integer, Pair<Type, Thunk<Value>>> futures = new TreeMap<>();
            for (int i = 0; i < tree.getChildren().size(); ++i) {
                NTree<Type, FuncId, Value> child = tree.getChildren().get(i);
                if (child.isEmpty()) {
                    throw new ThunkExecutionException("Cannot execute empty tree");
                } else if (child.isLeaf() || !executeMask.get(i)) {
                    // Already evaluated or evaluation not requested
                    unevaluated.put(i, child);
                } else {
                    Thunk<Value> thunk = thunkFactory.makeThunk(executor, child);
                    futures.put(i, new Pair<>(child.getType(), executor.submit(thunk)));
                }
            }
            return new TreeSusp<>(tree.getType(), tree.getLabel(), unevaluated, futures);
        }
    }
}
