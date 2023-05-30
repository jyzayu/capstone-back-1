package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.hashtag.dto.HashtagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryDto{
        String title;
        String weather;
        Set<HashtagDto> hashtag;
        String mood;
        String font;
        String thumbnail;
        List<BProperties> blocks;


        public Diary toEntity(){
                return Diary.of(title, weather, mood, font, thumbnail, blocks);
        }


        public static DiaryDto from(Diary diary){
                return new DiaryDto(diary.getTitle(), diary.getWeather(),
                        diary.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toUnmodifiableSet()),
                        diary.getMood(),
                        diary.getFont(),
                        diary.getThumbnail(),
                        diary.getBlocks());
        }

        public static DiaryDto of(String title, String weather, Set<HashtagDto> hashtag, String mood,String font, String thumbnail, List<BProperties> blocks) {
                return new DiaryDto(title, weather, hashtag, mood, font, thumbnail, blocks);
        }


}
