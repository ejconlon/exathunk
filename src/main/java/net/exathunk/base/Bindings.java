package net.exathunk.base;

import net.exathunk.functional.Either;
import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

import java.util.HashMap;
import java.util.Map;

public class Bindings {
    
    private final Bindings parent;
    private final Map<String, NTree<VarContType, FuncId, VarCont>> unevaled = new HashMap<>();
    private final Map<String, Pair<VarContType, VarCont>> evaled = new HashMap<>();
    
    private Bindings(Bindings parent) {
        this.parent = parent;
    }
    public Bindings() { this(null); }
    
    public Bindings newChild() {
        return new Bindings(this);
    }
    
    public Either<Pair<Bindings, NTree<VarContType, FuncId, VarCont>>, Pair<VarContType, VarCont>> getShallowest(String id) {
        if (evaled.containsKey(id)) return Either.AsRight(evaled.get(id));
        else if (unevaled.containsKey(id)) return Either.AsLeft(new Pair<>(this, unevaled.get(id)));
        else if (parent != null) return parent.getShallowest(id);
        else return null;
    }

    public VarContType getType(String id) {
        Either<Pair<Bindings, NTree<VarContType, FuncId, VarCont>>, Pair<VarContType, VarCont>> found = getShallowest(id);
        if (found != null) {
            if (found.isLeft()) {
                return found.left().getSecond().getType();
            } else {
                return found.right().getFirst();
            }
        } else {
            return null;
        }
    }
    
    public void setUnevaled(String id, NTree<VarContType, FuncId, VarCont> f) {
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
