package capstone.be.global.advice.exception.diary;

public class CDiarySearchPageInvalidException extends RuntimeException{
    public CDiarySearchPageInvalidException() {
    }

    public CDiarySearchPageInvalidException(String message) {
        super(message);
    }

    public CDiarySearchPageInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
