package net.exathunk.functional;

public interface Functor<A> {
    <B> Functor<B> fmap(Func1<A, B> f);
}