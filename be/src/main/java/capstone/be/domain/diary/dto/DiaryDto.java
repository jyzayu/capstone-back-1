package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryDto{
        String title;
        String weather;
        String hashtag;
        String mood;
        String font;
        List<BProperties> blocks;


        public Diary toEntity(){
                return Diary.of(title, weather, hashtag, mood,font,blocks);
        }

        public static DiaryDto of(String title, String weather, String hashtag, String mood,String font,List<BProperties> blocks) {
                return new DiaryDto(title, weather, hashtag, mood,font ,blocks);
        }

        public static DiaryDto from(Diary diary){
                return new DiaryDto(diary.getTitle(), diary.getWeather(), diary.getHashtag(), diary.getMood(),diary.getFont(), diary.getBlocks());
        }
}
