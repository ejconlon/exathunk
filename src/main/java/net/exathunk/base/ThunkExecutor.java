package net.exathunk.base;

import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

public interface ThunkExecutor {
    Thunk<VarCont> submit(Bindings bindings, NTree<VarContType, FuncId, VarCont> tree);
}
