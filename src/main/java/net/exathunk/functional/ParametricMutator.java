package net.exathunk.functional;

// The signature for an impure Func1.
// Mutates the mutee, obviously.
public interface ParametricMutator<Parameter, Mutee> {
    void mutate(Parameter param, Mutee mutee);
}