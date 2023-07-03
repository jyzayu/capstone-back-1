package capstone.be.global.advice.exception.diary;
//DIARY_002
public class CDiaryInvalidMoodException extends RuntimeException{
    public CDiaryInvalidMoodException() {
    }

    public CDiaryInvalidMoodException(String message) {
        super(message);
    }

    public CDiaryInvalidMoodException(String message, Throwable cause) {
        super(message, cause);
    }
}
