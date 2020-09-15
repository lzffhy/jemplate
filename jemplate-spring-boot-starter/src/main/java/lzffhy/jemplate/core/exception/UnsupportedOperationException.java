package lzffhy.jemplate.core.exception;

public class UnsupportedOperationException extends RuntimeException{

    public UnsupportedOperationException() {}

    public UnsupportedOperationException(String message) {
        super(message);
    }

    public UnsupportedOperationException(Throwable cause) {
        super(cause);
    }

    public UnsupportedOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
