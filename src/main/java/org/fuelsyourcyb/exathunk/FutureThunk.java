package org.fuelsyourcyb.exathunk;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.lang.InterruptedException;

public class FutureThunk<StateType, ResultType> implements Thunk<StateType, ResultType> {
    private final StateType state;
    private final Future<ResultType> futureResult;

    FutureThunk(StateType state, Future<ResultType> futureResult) {
	this.state = state;
	this.futureResult = futureResult;
    }

    public boolean isFinished() {
	return futureResult.isDone();
    }

    public void step() {}

    public StateType getState() {
	return state;
    }
    
    public ResultType getResult() {
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