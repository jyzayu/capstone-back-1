package capstone.be.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@Service
public class AmazonS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")

    public String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile).orElseThrow(() -> new IllegalArgumentException("파일 전환 실패"));

        return upload(uploadFile, dirName);
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();

    }


    //원본 이미지 링크를 받아서 썸네일을 만들도록 함.
    public String uploadThumbnail(String imageUrl, String dirName, int width, int height) throws IOException {
        // 원본 이미지를 로컬에 저장
        // S3에서 이미지 다운로드
        URL url = new URL(imageUrl);
        BufferedImage image = ImageIO.read(url);

        // 썸네일 이미지 생성
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        //원본 비율은 유지하되, 리사이즈 크기보다 작아서는 안됨.
        double widthRatio = (double) width / originalWidth;
        double heightRatio = (double) height / originalHeight;
        double scaleFactor = Math.max(widthRatio, heightRatio);

        int resizedWidth = (int) (originalWidth * scaleFactor);
        int resizedHeight = (int) (originalHeight * scaleFactor);

        BufferedImage thumbnailImage = Thumbnails.of(image)
                .size(resizedWidth, resizedHeight)
                .asBufferedImage();

        // 썸네일 이미지를 임시 파일로 저장
        File thumbnailFile = new File(System.getProperty("user.dir") + "/" + UUID.randomUUID() + "_thumbnail.jpg");
        ImageIO.write(thumbnailImage, "jpg", thumbnailFile);

        // 썸네일 이미지를 S3에 업로드
        String thumbnailUrl = upload(thumbnailFile, dirName);

        // 임시 파일 삭제
        removeNewFile(thumbnailFile);

        return thumbnailUrl;
    }

}
