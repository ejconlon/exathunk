package net.exathunk.base;

public class PresentThunk<Value> implements Thunk<Value> {
    private Value result;

    public PresentThunk(Value result) {
        this.result = result;
    }

    public void prepare() {}

    public void run() {}

    public Value get() {
        return result;
    }

    public boolean isDone() {
        return true;  // always already done
    }

    public String toString() {
        return "PresentThunk<"+result+">";
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Thunk)) return false;
        return ThunkUtils.statelessEquals(this, (Thunk<Value>)o);
    }
}
