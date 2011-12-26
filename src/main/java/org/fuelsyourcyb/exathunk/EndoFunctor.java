package org.fuelsyourcyb.exathunk;

// An endofunctor maps a category to itself.
// I'm squinting at this definition and applying it
// in the sense that it maps a type to itself (the correct
// sense) and also that it maps a data structure to itself.
// (In other words, it uses a pure function to mutate a
// structure.)
public interface EndoFunctor<A> {
    // Takes a Func1 instead of a ParametricMutator
    // because it would be a little confusing otherwise.
    // Performance-sensitive implementations can
    // modify their argument and return it if needed.
    void fmapInto(Func1<A, A> f);
}