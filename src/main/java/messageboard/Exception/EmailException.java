package messageboard.Exception;

public class EmailException extends RuntimeException{

    public EmailException() {
        super();
    }

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailException(Throwable cause) {
        super(cause);
    }

    protected EmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
