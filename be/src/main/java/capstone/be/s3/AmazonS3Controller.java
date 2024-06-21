package capstone.be.s3;


import capstone.be.global.advice.exception.s3.S3ImgFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor //final or @Not null 필드의 생성자를 자동으로 만들어줌
@RestController
@RequestMapping("/api")

public class AmazonS3Controller {

    private final AmazonS3Service amazonS3Service;
    private final Map<String, Object> response = new HashMap<>();


    private final int maxFileSize = 10240;

    //key 값으로 image 적어서 test하면됩니다 ^^
    //test시에 global > config > securityConfig >  securityFilterChain 함수  "/api/auth/**" 부분 "/api/**" 로 수정
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        int idx = multipartFile.getOriginalFilename().lastIndexOf('.');
        String imgformat = multipartFile.getOriginalFilename().substring(idx + 1);

        // 이미지 형식 검사해서 에러 출력하는 코드
        if (imgformat.equals("png") || imgformat.equals("jpg") || imgformat.equals("jpeg")) {
        }
        // IMAGE_002
        else {
            throw new S3ImgFormatException();
        }

        String imgUrl = amazonS3Service.upload(multipartFile, "thumbnails");


        URI location = ServletUriComponentsBuilder.fromPath(imgUrl).build().toUri();

        return ResponseEntity.created(URI.create("https:/"+imgUrl.substring(7))).build();
    }
}
