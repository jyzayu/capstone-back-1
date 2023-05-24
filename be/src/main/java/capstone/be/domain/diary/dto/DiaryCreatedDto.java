package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.Diary;

import java.time.LocalDateTime;

public record DiaryCreatedDto(
        String title,
        String weather,
        String hashtag,
        String mood,
        LocalDateTime date
){

    public Diary toEntity(){
        return Diary.of(title, weather, hashtag, mood);
    }

    public static DiaryCreatedDto from(Diary diary){
        return new DiaryCreatedDto(diary.getTitle(), diary.getWeather(), diary.getHashtag(), diary.getMood(), diary.getCreatedAt());
    }
}
