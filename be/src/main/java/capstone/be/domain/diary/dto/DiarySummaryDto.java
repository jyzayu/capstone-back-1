package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiarySummaryDto {
    private Long id;
    private String title;
    private String mood;
    private String date;
    private String weather;
    private String content;
    private List<BProperties> blocks;
    private LocalDateTime createdAt;

    public static DiarySummaryDto from(Diary diary){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = diary.getCreatedAt().format(formatter);

        StringBuilder sb = new StringBuilder();
        diary.getBlocks().stream().filter(x -> x.getType().equals("text")).forEach(x -> sb.append(x.getData().getText().replaceAll("<.*?>", "") + "\n"));

        if(diary.getTitle().equals("")){
            return new DiarySummaryDto(
                    diary.getId(),
                    diary.getBlocks().stream().filter(x -> x.getType().equals("text")).findFirst().get().getData().getText().replaceAll("<.*?>", ""),
                    diary.getMood(),
                    formattedDate,
                    diary.getWeather(),
                    sb.toString(),
                    diary.getBlocks(),
                    diary.getCreatedAt());
        }
        else{
            return new DiarySummaryDto(
                    diary.getId(),
                    diary.getTitle().replaceAll("<.*?>", ""),
                    diary.getMood(),
                    formattedDate,
                    diary.getWeather(),
                    sb.toString(),
                    diary.getBlocks(),
                    diary.getCreatedAt());
        }
    }

}
