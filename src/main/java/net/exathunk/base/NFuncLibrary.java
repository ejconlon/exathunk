package net.exathunk.base;

import net.exathunk.genthrift.FuncId;

public interface NFuncLibrary extends FuncDefLibrary {
    NFunc getFunc(FuncId funcId) throws UnknownFuncException;
}
