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
public class DiarySearchDto {
    private Long id;
    private String title;
    private String mood;
    private String weather;
//    private List<BProperties> blocks;
    private LocalDateTime createdAt;
    private String combinedBlockText;

    public static DiarySearchDto from(Diary diary){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = diary.getCreatedAt().format(formatter);
        if(diary == null){
            System.out.println("diary is null");
        }
        System.out.println(diary);
//        StringBuilder sb = new StringBuilder();
//        diary.getBlocks().stream().filter(x -> x.getType().equals("text")).forEach(x -> sb.append(x.getData().getText().replaceAll("<.*?>", "") + "\n"));

        if(diary.getTitle().equals("")){
            return new DiarySearchDto(
                    diary.getId(),
                    diary.getCombinedBlockText().replaceAll("<.*?>", ""),
                    diary.getMood(),
                    diary.getWeather(),
//                    diary.getBlocks(),
                    diary.getCreatedAt(),
                    diary.getCombinedBlockText());
        }
        else{
            return new DiarySearchDto(
                    diary.getId(),
                    diary.getTitle().replaceAll("<.*?>", ""),
                    diary.getMood(),
                    diary.getWeather(),
                    diary.getCreatedAt(),
                    diary.getCombinedBlockText());
        }
    }

}
