package capstone.be.global.advice.exception.diary;
//DIARY_003
public class CDiaryInvalidBlockException extends RuntimeException{
    public CDiaryInvalidBlockException() {
    }

    public CDiaryInvalidBlockException(String message) {
        super(message);
    }

    public CDiaryInvalidBlockException(String message, Throwable cause) {
        super(message, cause);
    }
}
