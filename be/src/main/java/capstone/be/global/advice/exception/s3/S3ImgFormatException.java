package capstone.be.global.advice.exception.s3;

public class S3ImgFormatException extends RuntimeException {
    public S3ImgFormatException() {
        super();
    }

    public S3ImgFormatException(String message) {
        super(message);
    }

    public S3ImgFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
