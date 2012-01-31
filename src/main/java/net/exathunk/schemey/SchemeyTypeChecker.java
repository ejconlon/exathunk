package net.exathunk.schemey;

import net.exathunk.base.*;
import net.exathunk.genthrift.ContType;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;
import net.exathunk.genthrift.VarType;

public class SchemeyTypeChecker implements TypeChecker {
    @Override
    public boolean canCast(VarContType fromType, VarContType toType) {
        // TODO allow upcasts
        return fromType.getContType().equals(toType.getContType()) &&
                fromType.getValueType().equals(toType.getValueType());
    }

    @Override
    public boolean canCast(VarContType type, VarCont value) {
        // TODO big ugly switch
        return true;
    }

    @Override
    public VarCont cast(VarContType type, String fromValue) throws TypeException {
        if (!type.getContType().equals(ContType.SINGLETON)) throw new TypeException("Need singleton cont type");
        if (type.getValueType().equals(VarType.BOOL)) {
            return VarUtils.makeBoolVarCont(Boolean.parseBoolean(fromValue));
        } else if (type.getValueType().equals(VarType.I64)) {
            try {
                return VarUtils.makeLongVarCont(Long.parseLong(fromValue));
            } catch (NumberFormatException e) {
                throw new TypeException(e);
            }
        } else if (type.getValueType().equals(VarType.STRING)) {
            return VarUtils.makeStringVarCont(fromValue);
        }
        throw new TypeException("TODO support more types");
    }
}