package lzffhy.jemplate.core.exception;

public class JdbcException extends RuntimeException {

    public JdbcException() {}

    public JdbcException(String message) {
        super(message);
    }

    public JdbcException(Throwable cause) {
        super(cause);
    }

    public JdbcException(String message, Throwable cause) {
        super(message, cause);
    }
}
