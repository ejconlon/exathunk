package net.exathunk.base;

import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public abstract class StrictNFuncImpl extends NFuncImpl {
    public StrictNFuncImpl(FuncDef funcDef) {
        super(funcDef);
    }

    @Override
    public Thunk<VarCont> invoke(final ThunkFactory thunkFactory, final ThunkExecutor<VarCont> executor,
                                 final NTree<VarContType, FuncId, VarCont> args) {
        assert args.isBranch();
        return new CallableThunk<>(new Callable<VarCont>() {
            @Override
            public VarCont call() throws Exception {
                List<Boolean> mask = new ArrayList<>(args.getChildren().size());
                for (int i = 0; i < args.getChildren().size(); ++i) { mask.add(true); }
                Thunk<NTree<VarContType, FuncId, VarCont>> evaled =
                        TreeExecutor.execute(thunkFactory, executor, args, mask);
                return subInvoke(evaled.get().extractChildValues());
            }
        });
    }
    
    protected abstract VarCont subInvoke(List<VarCont> values) throws ExecutionException;
}
