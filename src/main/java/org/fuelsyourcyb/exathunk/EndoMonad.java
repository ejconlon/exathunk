package org.fuelsyourcyb.exathunk;

public interface EndoMonad<Parameter, Mutee> {
    void bindInto(ParametricMutator<Parameter, Mutee> f);
}
