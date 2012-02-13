package net.exathunk.base;

import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

import java.util.HashMap;
import java.util.Map;

public class Bindings {
    
    private final Bindings parent;
    private final Map<String, NFunc> unevaled = new HashMap<>();
    private final Map<String, Pair<VarContType, VarCont>> evaled = new HashMap<>();
    
    private Bindings(Bindings parent) {
        this.parent = parent;
    }
    public Bindings() { this(null); }
    
    public Bindings newChild() {
        return new Bindings(this);
    }
    
    public NFunc getUnevaled(String id) {
        if (unevaled.containsKey(id)) return unevaled.get(id);
        else if (parent != null) return parent.getUnevaled(id);
        else return null;
    }
    
    public Pair<VarContType, VarCont> getEvaled(String id) {
        if (evaled.containsKey(id)) return evaled.get(id);
        else if (parent != null) return parent.getEvaled(id);
        else return null;
    }
    
    public void setUnevaled(String id, NFunc f) {
        unevaled.put(id, f);
    }
    
    public void setEvaled(String id, Pair<VarContType, VarCont> e) {
        evaled.put(id, e);
    }
    
    public boolean hasId(String id) {
        if (unevaled.containsKey(id) || evaled.containsKey(id)) {
            return true;
        } else if (parent != null) {
            return parent.hasId(id);
        } else {
            return false;
        }
    }
}
