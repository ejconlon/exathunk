package net.exathunk.base;

import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class TreeExecutor {
    public static Thunk<NTree<VarContType, FuncId, VarCont>> execute(
            ThunkExecutor executor,
            Bindings bindings,
            NTree<VarContType, FuncId, VarCont> tree,
            List<Boolean> executeMask) throws UnknownFuncException, ExecutionException {
        if (tree.isEmpty()) {
            throw new SystemExecutionException("Cannot execute empty tree");
        } else if (tree.isLeaf()) {
            return new PresentThunk<>(tree);
        } else {
            Map<Integer, NTree<VarContType, FuncId, VarCont>> unevaluated = new TreeMap<>();
            Map<Integer, Pair<VarContType, Thunk<VarCont>>> futures = new TreeMap<>();
            for (int i = 0; i < tree.getChildren().size(); ++i) {
                NTree<VarContType, FuncId, VarCont> child = tree.getChildren().get(i);
                if (child.isEmpty()) {
                    throw new SystemExecutionException("Cannot execute empty tree");
                } else if (child.isLeaf() || !executeMask.get(i)) {
                    // Already evaluated or evaluation not requested
                    unevaluated.put(i, child);
                } else {
                    futures.put(i, new Pair<>(child.getType(), executor.submit(bindings.newChild(), child)));
                }
            }
            return new TreeSusp<>(tree.getType(), tree.getLabel(), unevaluated, futures);
        }
    }
}
