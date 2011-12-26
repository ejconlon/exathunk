package org.fuelsyourcyb.exathunk;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.lang.InterruptedException;

public class FutureThunk<Value> implements Thunk<Value> {
    private final State state;
    private final Future<Value> futureResult;

    FutureThunk(State state, Future<Value> futureResult) {
	this.state = state;
	this.futureResult = futureResult;
    }

    public boolean isFinished() {
	return futureResult.isDone();
    }

    public void step() {}

    public State getState() {
	return state;
    }
    
    public Value getResult() {
	if (isFinished()) {
	    try {
		return futureResult.get();
	    } catch (InterruptedException impossible) {
		return null;
	    } catch (ExecutionException possible) {
		throw new ThunkEvaluationException("Error executing future",
						   possible);
	    } catch (CancellationException possible) {
		throw new ThunkEvaluationException("Future cancelled",
						   possible);
	    }
	} else {
	    return null;
	}
    }

}