package net.exathunk.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TreeSusp<Type, FuncId, Value> implements Thunk<NTree<Type, FuncId, Value>> {

    private final Type type;
    private final FuncId label;
    private final Map<Integer, NTree<Type, FuncId, Value>> unevaluated;
    private final Map<Integer, Pair<Type, Thunk<Value>>> futures;

    public TreeSusp(Type type, FuncId label,
                    Map<Integer, NTree<Type, FuncId, Value>> unevaluated,
                    Map<Integer, Pair<Type, Thunk<Value>>> futures) {
        this.type = type;
        this.label = label;
        this.unevaluated = unevaluated;
        this.futures = futures;
    }

    @Override
    public void run() {
        // Block on each sub-computation til they're done
        for (Pair<Type, Thunk<Value>> pair : futures.values()) {
            try {
                pair.getSecond().get();
            } catch (ExecutionException e) {
                // it'll be caught in this get
            }
        }
    }

    @Override
    public boolean isDone() {
        for (Pair<Type, Thunk<Value>> pair : futures.values()) {
            if (!pair.getSecond().isDone()) return false;
        }
        return true;
    }

    @Override
    public NTree<Type, FuncId, Value> get() throws ExecutionException {
        final int numChildren = unevaluated.size() + futures.size();
        List<NTree<Type, FuncId, Value>> children = new ArrayList<>(numChildren);
        for (int i = 0; i < numChildren; ++i) {
            if (unevaluated.containsKey(i)) {
                children.add(unevaluated.get(i));
            } else if (futures.containsKey(i)) {
                Pair<Type, Thunk<Value>> pair = futures.get(i);
                Value value = pair.getSecond().get();
                children.add(new NTree<Type, FuncId, Value>(pair.getFirst(), value));
            } else {
                throw new ExecutionException(new IllegalArgumentException("Could not find child "+i+" of "+numChildren));
            }
        }
        return new NTree<>(type, label, children);
    }
}