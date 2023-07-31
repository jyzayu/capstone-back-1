package capstone.be.domain.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryMainTotalResponse {
    private Long year;
    private Long month;

    public static DiaryMainTotalResponse of(Long year, Long month) {
        return new DiaryMainTotalResponse(year, month);
    }
}
