package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;

import java.time.LocalDateTime;
import java.util.List;

public record DiaryCreatedDto(
        String title,
        String weather,
        String hashtag,
        String mood,

        String font,

        List<BProperties> blocks,
        LocalDateTime date
){

    public Diary toEntity(){
        return Diary.of(title, weather, hashtag, mood,font ,blocks);
    }

    public static DiaryCreatedDto from(Diary diary){
        return new DiaryCreatedDto(diary.getTitle(), diary.getWeather(), diary.getHashtag(), diary.getMood(), diary.getFont(),diary.getBlocks(),diary.getCreatedAt());
    }
}
