package org.fuelsyourcyb.exathunk;

// The signature for an impure Func1.
// Mutates the mutee, obviously.
public interface ParametricMutator<Parameter, Mutee> {
    void mutate(Parameter param, Mutee mutee);
}