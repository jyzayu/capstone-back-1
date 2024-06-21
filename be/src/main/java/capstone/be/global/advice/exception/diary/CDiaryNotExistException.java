package capstone.be.global.advice.exception.diary;

public class CDiaryNotExistException extends RuntimeException{
    public CDiaryNotExistException() {
    }

    public CDiaryNotExistException(String message) {
        super(message);
    }

    public CDiaryNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
