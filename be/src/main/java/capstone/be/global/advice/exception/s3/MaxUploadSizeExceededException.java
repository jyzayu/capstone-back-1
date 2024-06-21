package capstone.be.global.advice.exception.s3;

public class MaxUploadSizeExceededException extends RuntimeException {
    public MaxUploadSizeExceededException() {
        super();
    }

    public MaxUploadSizeExceededException(String message) {
        super(message);
    }

    public MaxUploadSizeExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
