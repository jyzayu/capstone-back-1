package capstone.be.global.advice.exception;

public class CUserNotFound2Exception extends RuntimeException{
    public CUserNotFound2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public CUserNotFound2Exception(String message) {
        super(message);
    }

    public CUserNotFound2Exception() {
        super();
    }
}
