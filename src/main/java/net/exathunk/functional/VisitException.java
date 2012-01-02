package net.exathunk.functional;

public class VisitException extends Exception {
    public VisitException(String message) {
        super(message);
    }

    public VisitException(Throwable cause) {
        super(cause);
    }

    public VisitException (String message, Throwable cause) {
        super(message, cause);
    }
}