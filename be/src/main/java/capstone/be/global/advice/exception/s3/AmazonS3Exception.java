package capstone.be.global.advice.exception.s3;

public class AmazonS3Exception extends RuntimeException{
    public AmazonS3Exception() {
        super();
    }

    public AmazonS3Exception(String message) {
        super(message);
    }

    public AmazonS3Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
