package net.exathunk.base;

import java.util.concurrent.ExecutionException;

public class SystemExecutionException extends ExecutionException {
    public SystemExecutionException(String message) {
        super(message);
    }

    public SystemExecutionException(Throwable cause) {
        super(cause);
    }

    public SystemExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}