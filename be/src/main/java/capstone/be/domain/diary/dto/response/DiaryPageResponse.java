package capstone.be.domain.diary.dto.response;

import capstone.be.domain.diary.domain.Diary;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"pageNumber", "isLastPage", "list"})
public class DiaryPageResponse {


    private int pageNumber;
    @JsonProperty("isLastPage")
    private boolean isLastPage;
    private List<DiaryContentSearchResponse> list;

    public static DiaryPageResponse from(List<DiaryContentSearchResponse> res,int page,boolean isLastPage) {


        return new DiaryPageResponse(
                page,
                isLastPage,
                res
        );
    }
}
