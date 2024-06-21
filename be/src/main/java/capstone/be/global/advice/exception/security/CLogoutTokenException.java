package capstone.be.global.advice.exception.security;


public class CLogoutTokenException extends RuntimeException {
    public CLogoutTokenException() {
        super();
    }

    public CLogoutTokenException(String message) {
        super(message);
    }

    public CLogoutTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
