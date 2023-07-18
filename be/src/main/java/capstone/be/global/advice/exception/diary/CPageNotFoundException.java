package capstone.be.global.advice.exception.diary;

public class CPageNotFoundException extends RuntimeException{
        public CPageNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public CPageNotFoundException(String message) {
            super(message);
        }

        public CPageNotFoundException() {
            super();
        }
}
