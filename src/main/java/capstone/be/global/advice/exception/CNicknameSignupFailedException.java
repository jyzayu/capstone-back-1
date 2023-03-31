package capstone.be.global.advice.exception;

public class CNicknameSignupFailedException extends RuntimeException{
    public CNicknameSignupFailedException() {
        super();
    }

    public CNicknameSignupFailedException(String message) {
        super(message);
    }

    public CNicknameSignupFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
