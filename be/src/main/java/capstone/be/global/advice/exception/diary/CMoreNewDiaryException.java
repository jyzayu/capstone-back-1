package capstone.be.global.advice.exception.diary;

public class CMoreNewDiaryException extends RuntimeException{
    public CMoreNewDiaryException() {
    }

    public CMoreNewDiaryException(String message) {
        super(message);
    }

    public CMoreNewDiaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
