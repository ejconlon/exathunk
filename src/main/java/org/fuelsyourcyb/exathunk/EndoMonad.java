package org.fuelsyourcyb.exathunk;

// See comments on EndoFunctor. The op I care about here
// is bind, since join would be pretty ugly and mostly
// inapplicable.  Since it's "endo," the func appliction is
// going to mutate in place.
public interface EndoMonad<Parameter, Mutee> {
    void bindInto(ParametricMutator<Parameter, Mutee> f);
}
