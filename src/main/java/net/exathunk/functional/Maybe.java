package net.exathunk.functional;

public class Maybe<Type> {
    private Type value;

    public Maybe(Type value) {
        this.value = value;
    }

    public static <T> Maybe<T> Just(T value) {
        return new Maybe<>(value);
    }

    public static <T> Maybe<T> Nothing() {
        return new Maybe<>(null);
    }

    public boolean isJust() {
        return value != null;
    }

    public boolean isNothing() {
        return value == null;
    }

    public Type just() {
        return value;
    }

    public boolean equals(Maybe<Type> o) {
        if (o == null) return false;
        if (value == null) {
            return o.value == null;
        } else {
            if (o.value == null) {
                return false;
            } else {
                return value.equals(o.value);
            }
        }
    }

}