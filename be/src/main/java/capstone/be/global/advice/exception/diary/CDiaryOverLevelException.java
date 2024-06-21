package capstone.be.global.advice.exception.diary;
//DIARY_007
public class CDiaryOverLevelException extends RuntimeException{
    public CDiaryOverLevelException() {
    }

    public CDiaryOverLevelException(String message) {
        super(message);
    }

    public CDiaryOverLevelException(String message, Throwable cause) {
        super(message, cause);
    }
}
