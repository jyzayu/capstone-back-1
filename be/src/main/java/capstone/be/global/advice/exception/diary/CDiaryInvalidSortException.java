package capstone.be.global.advice.exception.diary;
//DIARY_004
public class CDiaryInvalidSortException extends RuntimeException{
    public CDiaryInvalidSortException() {
    }

    public CDiaryInvalidSortException(String message) {
        super(message);
    }

    public CDiaryInvalidSortException(String message, Throwable cause) {
        super(message, cause);
    }
}
