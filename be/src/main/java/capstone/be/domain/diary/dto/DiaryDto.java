package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.hashtag.dto.HashtagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryDto{
        String title;
        String weather;
        Set<HashtagDto> hashtag;
        String mood;


        public Diary toEntity(){
                return Diary.of(title, weather, mood);
        }

        public static DiaryDto of(String title, String weather, Set<HashtagDto> hashtag, String mood) {
                return new DiaryDto(title, weather, hashtag, mood);
        }

        public static DiaryDto from(Diary diary){
                return new DiaryDto(diary.getTitle(), diary.getWeather(),
                        diary.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toUnmodifiableSet()),
                        diary.getMood());
        }
}
