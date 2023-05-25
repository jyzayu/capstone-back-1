package capstone.be.domain.diary.dto.response;

import capstone.be.domain.diary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryMoodSearchResponse {
    private Long id;
    private String title;
    private LocalDateTime createAt;

    public static DiaryMoodSearchResponse from(final Diary diary) {
        return new DiaryMoodSearchResponse(
                diary.getId(),
                diary.getTitle(),
                diary.getCreatedAt()
        );
    }
}
