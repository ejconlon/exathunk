package net.exathunk.base;

import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;

import java.util.List;

public interface FuncDefLibrary {
    boolean knowsFunc(FuncId funcId);

    FuncDef getFuncDef(FuncId funcId) throws  UnknownFuncException;
    
    List<FuncDef> getFuncDefs(List<FuncId> funcIds) throws UnknownFuncException;
}
