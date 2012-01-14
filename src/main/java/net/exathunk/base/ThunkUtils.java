package net.exathunk.base;

import net.exathunk.functional.VisitException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
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


    


    public static class StepVisitor<Type, FuncId, Value> implements NTree.Visitor<Type, FuncId, Thunk<Value>> {
        private final ThunkFactory<Type, FuncId, Value> factory;
        private final ThunkExecutor<Value> executor;

        public StepVisitor(ThunkFactory<Type, FuncId, Value> factory, ThunkExecutor<Value> executor) {
            this.factory = factory;
            this.executor = executor;
        }

        protected void evaluateThunk(Thunk<Value> thunk) throws VisitException {
            thunk.run();
        }

        public void visit(NTree<Type, FuncId, Thunk<Value>> tree) throws VisitException {
            try {
                if (ThunkUtils.isEvaluable(tree)) {
                    Thunk<Value> thunk = factory.makeThunk(executor, unthunkValues(tree));
                    tree.setLeaf(tree.getType(), thunk);
                } else if (tree.isLeaf() && !tree.getValue().isDone()) {
                    evaluateThunk(tree.getValue());
                }
            } catch (ExecutionException e) {
                throw new VisitException(e);
            } catch (InterruptedException e) {
                throw new VisitException(e);
            } catch (UnknownFuncException e) {
                throw new VisitException(e);
            }
        }

        public void start() {}

        public void end() {}
    }

}