package capstone.be.domain.diary.dto.response;

import capstone.be.domain.diary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryMoodSearchResponse {
    private Long id;
    private String title;
    private String thumbnail;

    private String createAt;

    public static DiaryMoodSearchResponse from(final Diary diary) {
        //문자열 형태로 변환해달라는 프론트의 요청으로 response format 수정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy.MM.dd");
        String formattedDate = diary.getCreatedAt().format(formatter);
        return new DiaryMoodSearchResponse(
                diary.getId(),
                diary.getTitle(),
                diary.getThumbnail(),
                formattedDate
        );
    }
}
