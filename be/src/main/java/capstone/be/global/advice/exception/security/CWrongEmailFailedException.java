package capstone.be.global.advice.exception.security;

public class CWrongEmailFailedException extends RuntimeException{
    public CWrongEmailFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CWrongEmailFailedException(String message) {
        super(message);
    }

    public CWrongEmailFailedException() {
        super();
    }
}
