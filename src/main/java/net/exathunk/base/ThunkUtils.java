package net.exathunk.base;

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
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
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

    public static <Type, FuncId, Value> List<Value> unthunkValues(NTree<Type, FuncId, Thunk<Value>> tree) throws ExecutionException, InterruptedException {
        List<Value> params = new ArrayList<>(tree.getChildren().size());
        for (NTree<Type, FuncId, Thunk<Value>> child : tree.getChildren()) {
            Thunk<Value> thunk = child.getValue();
            params.add(thunk.get());
        }
        return params;
    }


    public static <Type, FuncId, Value> NTree<Type, FuncId, Thunk<Value>>  makeThunkTree(
            ThunkFactory<Type, FuncId, Value> thunkFactory,
            NTree<Type, FuncId, Value> typedTree) throws UnknownFuncException, ExecutionException {
        if (typedTree.isEmpty()) {
            throw new UnknownFuncException("Empty computation");
        } else if (typedTree.isLeaf()) {
            return new NTree<Type, FuncId, Thunk<Value>>(
                    typedTree.getType(),
                    new PresentThunk<>(typedTree.getValue()));
        } else if (typedTree.isTerminalBranch()) {
            Thunk<Value> thunk = thunkFactory.makeThunk(typedTree.getLabel(), typedTree.extractChildValues());
            return new NTree<>(typedTree.getType(), thunk);
        } else {
            List<NTree<Type, FuncId, Thunk<Value>>> thunkedChildren =
                    new ArrayList<>(typedTree.getChildren().size());
            for (NTree<Type, FuncId, Value> child : typedTree.getChildren()) {
                thunkedChildren.add(makeThunkTree(thunkFactory, child));
            }
            return new NTree<>(typedTree.getType(), typedTree.getLabel(), thunkedChildren);
        }
    }

    public static class StepVisitor<Type, FuncId, Value> implements NTree.Visitor<Type, FuncId, Thunk<Value>> {
        private final ThunkFactory<Type, FuncId, Value> factory;

        public StepVisitor(ThunkFactory<Type, FuncId, Value> factory) {
            this.factory = factory;
        }

        protected void evaluateThunk(Thunk<Value> thunk) throws VisitException {
            thunk.run();
        }

        public void visit(NTree<Type, FuncId, Thunk<Value>> tree) throws VisitException {
            try {
                if (ThunkUtils.isEvaluable(tree)) {
                    Thunk<Value> thunk = factory.makeThunk(tree.getLabel(), ThunkUtils.unthunkValues(tree));
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