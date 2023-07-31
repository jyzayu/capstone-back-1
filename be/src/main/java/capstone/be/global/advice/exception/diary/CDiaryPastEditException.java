package capstone.be.global.advice.exception.diary;

public class CDiaryPastEditException extends RuntimeException{
    public CDiaryPastEditException() {
    }

    public CDiaryPastEditException(String message) {
        super(message);
    }

    public CDiaryPastEditException(String message, Throwable cause) {
        super(message, cause);
    }
}
