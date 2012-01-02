package net.exathunk.base;

import java.util.concurrent.ExecutionException;

public class ThunkExecutionException extends ExecutionException {
    public ThunkExecutionException(String message) {
        super(message);
    }

    public ThunkExecutionException(Throwable cause) {
        super(cause);
    }

    public ThunkExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}