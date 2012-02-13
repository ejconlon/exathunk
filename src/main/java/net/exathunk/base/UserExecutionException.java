package net.exathunk.base;

import java.util.concurrent.ExecutionException;

public class UserExecutionException extends ExecutionException {
    public UserExecutionException(String message) {
        super(message);
    }

    public UserExecutionException(Throwable cause) {
        super(cause);
    }

    public UserExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
