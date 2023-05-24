package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.hashtag.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record DiaryCreatedDto(
        String title,
        String weather,
        Set<HashtagDto> hashtag,
        String mood,
        LocalDateTime date
){

    public Diary toEntity(){
        return Diary.of(title, weather, mood);
    }

    public static DiaryCreatedDto from(Diary diary){
        return new DiaryCreatedDto(diary.getTitle(), diary.getWeather(),
                diary.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toUnmodifiableSet()),
                diary.getMood(), diary.getCreatedAt());
    }
}
