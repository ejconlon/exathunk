package net.exathunk.base;

import net.exathunk.genthrift.*;

public class VarUtils {
    public static VarCont makeVarCont(Var var) {
        VarCont varCont = new VarCont();
        varCont.setSingletonCont(var);
        return varCont;
    }

    public static VarCont makeLongVarCont(long val) {
        Var var = new Var();
        var.setI64Var(val);
        return makeVarCont(var);
    }

    public static VarCont makeBoolVarCont(boolean val) {
        Var var = new Var();
        var.setBoolVar(val);
        return makeVarCont(var);
    }

    public static VarCont makeStringVarCont(String val) {
        Var var = new Var();
        var.setStringVar(val);
        return makeVarCont(var);
    }
    
    public static VarContType makeLongVarContType() {
        VarContType type = new VarContType();
        type.setValueType(VarType.I64);
        type.setContType(ContType.SINGLETON);
        return type;
    }

    public static VarContType makeBoolVarContType() {
        VarContType type = new VarContType();
        type.setValueType(VarType.BOOL);
        type.setContType(ContType.SINGLETON);
        return type;
    }

    public static VarContType makeStringVarContType() {
        VarContType type = new VarContType();
        type.setValueType(VarType.STRING);
        type.setContType(ContType.SINGLETON);
        return type;
    }
}
