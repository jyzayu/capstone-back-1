package capstone.be.domain.diary.dto.response;

import capstone.be.domain.diary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarResponse {
    private Long id;
    private String date;
    private String mood;
    private String title;


    public static CalendarResponse from(final Diary diary) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = diary.getCreatedAt().format(formatter);

        return new CalendarResponse(
                diary.getId(),
                formattedDate,
                diary.getMood(),
                diary.getTitle()

        );
    }
}
