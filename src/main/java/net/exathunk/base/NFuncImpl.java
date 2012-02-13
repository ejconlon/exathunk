package net.exathunk.base;

import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class NFuncImpl implements NFunc {
    
    private final FuncDef funcDef;

    public NFuncImpl(FuncDef funcDef) {
        this.funcDef = funcDef;
    }

    @Override
    public FuncDef getFuncDef() { return funcDef; }

    @Override
    public abstract Thunk<VarCont> invoke(NFuncLibrary library,
                                          ThunkExecutor executor,
                                          Bindings bindings,
                                          NTree<VarContType, FuncId, VarCont> args);
}
