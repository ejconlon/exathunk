package net.exathunk.base;

import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

public class DefaultThunkExecutor implements ThunkExecutor {

    private final NFuncLibrary library;

    public DefaultThunkExecutor(NFuncLibrary library) {
        this.library = library;
    }

    @Override
    public Thunk<VarCont> submit(Bindings bindings, NTree<VarContType, FuncId, VarCont> tree) {
        FuncId funcId = tree.getLabel();
        NFunc func;
        try {
            func = library.getFunc(funcId);
        } catch (UnknownFuncException e) {
            return new BottomThunk<>(e);
        }
        Thunk<VarCont> thunk = func.invoke(library, this, bindings, tree);
        thunk.run();
        return thunk;
    }
}
