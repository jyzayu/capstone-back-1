package capstone.be.domain.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryMoodTotalResponse {
    private long best;
    private long good;
    private long normal;
    private long bad;
    private long worst;
}