package org.fuelsyourcyb.exathunk;

import java.util.concurrent.ExecutionException;

public class ThunkEvaluationException extends ExecutionException {
    public ThunkEvaluationException(String message) {
	super(message);
    }

    public ThunkEvaluationException(String message, Throwable cause) {
	super(message, cause);
    }
}