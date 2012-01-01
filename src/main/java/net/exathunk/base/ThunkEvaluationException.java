package net.exathunk.base;

import java.util.concurrent.ExecutionException;

public class ThunkEvaluationException extends ExecutionException {
    public ThunkEvaluationException(String message) {
        super(message);
    }

    public ThunkEvaluationException(Throwable cause) {
        super(cause);
    }

    public ThunkEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}