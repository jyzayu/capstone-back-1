package capstone.be.s3;

import capstone.be.global.advice.exception.s3.S3ImgFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public String upload(@RequestParam("image") MultipartFile multipartFile)throws IOException {

        int idx = multipartFile.getOriginalFilename().lastIndexOf('.');
        String imgformat = multipartFile.getOriginalFilename().substring(idx + 1);

        // 이미지 형식 검사해서 에러 출력하는 코드
        if (imgformat.equals("png") || imgformat.equals("jpg") || imgformat.equals("jpeg")) {
        }
        // IMAGE_002
        else{
            throw new S3ImgFormatException();
        }

        String imgUrl = amazonS3Service.upload(multipartFile, "static");
        //썸네일 사진 생성. 일단 저장만 구현해둠. 블록 나오면 제일 1번째 이미지 블록에 나오는 걸 썸네일로 골라서 저장해야 할 것 같음..
        String thumbnailUrl = amazonS3Service.uploadThumbnail(multipartFile, "thumbnails", 300, 400);

        return imgUrl;
    }

}
