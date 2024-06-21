package capstone.be.global.advice.exception.security;


public class CUserExistException extends RuntimeException {
    public CUserExistException() {
        super();
    }

    public CUserExistException(String message) {
        super(message);
    }

    public CUserExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
