package capstone.be.global.advice.exception.diary;

public class CSearchLogNotFoundException extends RuntimeException{
    public CSearchLogNotFoundException() {
    }

    public CSearchLogNotFoundException(String message) {
        super(message);
    }

    public CSearchLogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

