package capstone.be.global.advice.exception.calendar;

public class CDiaryCalendarException extends RuntimeException{
    public CDiaryCalendarException() {
    }

    public CDiaryCalendarException(String message) {
        super(message);
    }

    public CDiaryCalendarException(String message, Throwable cause) {
        super(message, cause);
    }
}
