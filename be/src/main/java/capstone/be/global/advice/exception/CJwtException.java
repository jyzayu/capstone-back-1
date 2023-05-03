package capstone.be.global.advice.exception;

public class CJwtException extends RuntimeException{
    public CJwtException() {
        super();
    }

    public CJwtException(String message) {
        super(message);
    }

    public CJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}

