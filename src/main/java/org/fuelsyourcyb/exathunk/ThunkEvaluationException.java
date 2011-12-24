package org.fuelsyourcyb.exathunk;

public class ThunkEvaluationException extends RuntimeException {
    public ThunkEvaluationException(String message) {
	super(message);
    }

    public ThunkEvaluationException(String message, Exception cause) {
	super(message, cause);
    }
}