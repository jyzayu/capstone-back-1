package capstone.be.domain.diary.dto.response;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.hashtag.domain.Hashtag;
import capstone.be.domain.hashtag.dto.HashtagDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;




@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryContentSearchResponse {
    private Long diaryId;
    private String title;
    private String createAt;
    private String context;
    private Set<String> hashtag;




    public static DiaryContentSearchResponse from(final Diary diary) {
        //문자열 형태로 변환해달라는 프론트의 요청으로 response format 수정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = diary.getCreatedAt().format(formatter);
        String contents ;


        if (diary.getBlocks().get(0).getType().equals("text")|| diary.getBlocks().get(0).getType().equals("heading"))
            contents = diary.getBlocks().get(0).getData().getText();
        else
            contents = null;

        return new DiaryContentSearchResponse(
                diary.getId(),
                diary.getTitle(),
                formattedDate,
                contents,
                diary.getHashtags().stream().map(Hashtag::getHashtagName).collect(Collectors.toUnmodifiableSet())
        );
    }
}

// 내용 검색 추가하고
// 검색시 해시태그 정보 추가로 나오게 하고
// 사용자 정보 받아서 검색하는 방식 추가하기바람