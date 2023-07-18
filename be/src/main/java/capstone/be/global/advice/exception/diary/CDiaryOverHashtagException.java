package capstone.be.global.advice.exception.diary;
//Diary_001
public class CDiaryOverHashtagException extends RuntimeException{
    public CDiaryOverHashtagException() {
    }

    public CDiaryOverHashtagException(String message) {
        super(message);
    }

    public CDiaryOverHashtagException(String message, Throwable cause) {
        super(message, cause);
    }
}
