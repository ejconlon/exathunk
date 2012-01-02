package net.exathunk.base;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public abstract class StrictNFuncImpl<Type, Label, Value> extends NFuncImpl<Type, Label, Value> {
    public StrictNFuncImpl(Type returnType, Type[] parameterTypes) {
        super(returnType, parameterTypes, makeStrictnesses(parameterTypes.length));
    }

    private static Strictness[] makeStrictnesses(int length) {
        Strictness[] strictnesses = new Strictness[length];
        for (int i = 0; i < length; ++i) strictnesses[i] = Strictness.STRICT;
        return strictnesses;
    }

    @Override
    public Thunk<Value> invoke(final ThunkFactory<Type, Label, Value> thunkFactory, final ThunkExecutor<Value> executor, final NTree<Type, Label, Value> args) {
        assert args.isBranch();
        return new CallableThunk<>(new Callable<Value>() {
            @Override
            public Value call() throws Exception {
                NTree<Type, Label, Value> execTree = ThunkUtils.execute(thunkFactory, executor, args);
                return subInvoke(execTree.extractChildValues());
            }
        });
    }
    
    protected abstract Value subInvoke(List<Value> values) throws ExecutionException;
}
