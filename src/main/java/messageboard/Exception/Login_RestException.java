package messageboard.Exception;


/**
 * 로그인 하지않은 다른 유저의 정보를 사용할때 발생하는 오류
 * rest api 타입 Exception
 */
public class Login_RestException extends RuntimeException{

    public Login_RestException() {
        super();
    }

    public Login_RestException(String message) {
        super(message);
    }

    public Login_RestException(String message, Throwable cause) {
        super(message, cause);
    }

    public Login_RestException(Throwable cause) {
        super(cause);
    }

    protected Login_RestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
