package net.exathunk.base;

import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

public interface NFunc {
    FuncDef getFuncDef();
    Thunk<VarCont> invoke(NFuncLibrary library,
                          ThunkExecutor executor,
                          Bindings bindings,
                          NTree<VarContType, FuncId, VarCont> args);
}