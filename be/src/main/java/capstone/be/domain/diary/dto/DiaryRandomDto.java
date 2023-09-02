package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.hashtag.dto.HashtagDto;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryRandomDto {
    private Long id;
    private String title;
    private String mood;
    private String date;
    private String weather;
    private String content;

    public static DiaryRandomDto from(Diary diary){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = diary.getCreatedAt().format(formatter);

        StringBuilder sb = new StringBuilder();
        diary.getBlocks().stream().filter(x -> x.getType().equals("text")).forEach(x -> sb.append(x.getData().getText().replaceAll("<.*?>", "") + "\n"));

        if(diary.getTitle().equals("")){
            return new DiaryRandomDto(
                    diary.getId(),
                    diary.getBlocks().stream().filter(x -> x.getType().equals("text")).findFirst().get().getData().getText(),
                    diary.getMood(),
                    formattedDate,
                    diary.getWeather(),
                    sb.toString());
        }
        else{
            return new DiaryRandomDto(
                    diary.getId(),
                    diary.getTitle(),
                    diary.getMood(),
                    formattedDate,
                    diary.getWeather(),
                    sb.toString());
        }
    }

}
