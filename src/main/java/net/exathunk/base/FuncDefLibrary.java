package net.exathunk.base;

import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;

public interface FuncDefLibrary {
    boolean knowsFunc(FuncId funcId);

    FuncDef getFuncDef(FuncId funcId) throws  UnknownFuncException;
}
