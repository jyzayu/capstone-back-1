package capstone.be.global.advice.exception.diary;

public class CDiaryInvalidFontException extends RuntimeException{
    public CDiaryInvalidFontException() {
    }

    public CDiaryInvalidFontException(String message) {
        super(message);
    }

    public CDiaryInvalidFontException(String message, Throwable cause) {
        super(message, cause);
    }
}
