package capstone.be.global.advice.exception.diary;

public class CDiaryNotFoundLinkException  extends RuntimeException{
    public CDiaryNotFoundLinkException() {
    }

    public CDiaryNotFoundLinkException(String message) {
        super(message);
    }

    public CDiaryNotFoundLinkException(String message, Throwable cause) {
        super(message, cause);
    }
}
