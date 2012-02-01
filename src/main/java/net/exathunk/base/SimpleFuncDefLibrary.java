package net.exathunk.base;

import net.exathunk.functional.FMap;
import net.exathunk.functional.Func1;
import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleFuncDefLibrary implements FuncDefLibrary{
    
    private final Map<String, FuncDef> defs = new HashMap<>();
    
    public SimpleFuncDefLibrary(List<FuncDef> defList) {
        for (FuncDef def : defList) {
            defs.put(def.getFuncId().getName(), def);
        }
    }
    
    @Override
    public boolean knowsFunc(FuncId funcId) {
        return defs.containsKey(funcId.getName());
    }

    @Override
    public FuncDef getFuncDef(FuncId funcId) throws UnknownFuncException {
        FuncDef def = defs.get(funcId.getName());
        if (def == null) throw new UnknownFuncException("Don't know func "+funcId.getName());
        return def;
    }

    @Override
    public List<FuncDef> getFuncDefs(List<FuncId> funcIds) throws UnknownFuncException {
        List<FuncDef> defList = new ArrayList<>(funcIds.size());
        for (FuncId funcId: funcIds) { defList.add(getFuncDef(funcId)); }
        return defList;
    }
}
