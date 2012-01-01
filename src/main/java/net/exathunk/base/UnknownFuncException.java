package net.exathunk.base;

public class UnknownFuncException extends Exception {
    public UnknownFuncException(String message) {
        super(message);
    }
    public UnknownFuncException(String message, Throwable cause) {
        super(message, cause);
    }
}
