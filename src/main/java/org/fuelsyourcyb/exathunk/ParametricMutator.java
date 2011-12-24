package org.fuelsyourcyb.exathunk;

public interface ParametricMutator<Parameter, Mutee> {
    void mutate(Parameter param, Mutee mutee);
}