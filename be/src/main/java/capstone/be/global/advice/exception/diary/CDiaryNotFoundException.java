package capstone.be.global.advice.exception.diary;

public class CDiaryNotFoundException extends RuntimeException {
    public CDiaryNotFoundException() {
    }

    public CDiaryNotFoundException(String message) {
        super(message);
    }

    public CDiaryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
