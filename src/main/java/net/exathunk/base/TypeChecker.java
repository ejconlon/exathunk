package net.exathunk.base;

import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

public interface TypeChecker {
    boolean canCast(VarContType fromType, VarContType toType);

    VarCont cast(VarContType toType, String fromValue) throws TypeException;
}