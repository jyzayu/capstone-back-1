package capstone.be.domain.like.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeRequestDto {

    private Long diaryId;

    public LikeRequestDto( Long diaryId) {
        this.diaryId = diaryId;
    }
}