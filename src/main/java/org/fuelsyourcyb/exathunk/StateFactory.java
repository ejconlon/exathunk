package org.fuelsyourcyb.exathunk;

public interface StateFactory<State> {
    State makeInitialState();
}