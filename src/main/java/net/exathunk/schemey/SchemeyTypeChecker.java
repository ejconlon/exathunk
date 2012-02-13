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
	return TypeCheckerUtils.typeEquals(fromType, toType);
    }

    @Override
    public boolean canCast(VarContType type, VarCont value) {
        // TODO big ugly switch
        return true;
    }

    @Override
    public Pair<VarContType, VarCont> cast(VarContType type, String fromValue) throws TypeException {
        if (!type.getContType().equals(ContType.SINGLETON)) throw new TypeException("Need singleton cont type");
        if (type.getValueType().equals(VarType.BOOL)) {
            return new Pair<>(type, VarUtils.makeBoolVarCont(Boolean.parseBoolean(fromValue)));
        } else if (type.getValueType().equals(VarType.I64)) {
            try {
                return new Pair<>(type, VarUtils.makeLongVarCont(Long.parseLong(fromValue)));
            } catch (NumberFormatException e) {
                throw new TypeException(e);
            }
        } else if (type.getValueType().equals(VarType.STRING)) {
            return new Pair<>(type, VarUtils.makeStringVarCont(fromValue));
        } else if (type.getValueType().equals(VarType.VOID) && type.isSetTemplateName()) {
            int coldex = fromValue.indexOf(':');
            if (coldex < 0) 
                throw new TypeException("No type specified: "+fromValue);
            else if (coldex == 0 || coldex == fromValue.length()-1) 
                throw new TypeException("Bad type/value: "+fromValue);
            String typeName = fromValue.substring(0, coldex);
            String realValue = fromValue.substring(coldex+1);
          
            VarContType newType = new VarContType(type);
            VarCont varCont = null;
            switch (typeName) {
                case "BOOL":
                    newType.setValueType(VarType.BOOL);
                    varCont = VarUtils.makeBoolVarCont(Boolean.parseBoolean(realValue));
                    break;
                case "I64":
                    newType.setValueType(VarType.I64);
                    varCont = VarUtils.makeLongVarCont(Long.parseLong(realValue));
                    break;
                case "STRING":
                    newType.setValueType(VarType.STRING);
                    varCont = VarUtils.makeStringVarCont(realValue);
                    break;
            }
            
            if (varCont != null) {
                return new Pair<>(newType, varCont);
            }
        }
        throw new TypeException("TODO support more types");
    }
}
