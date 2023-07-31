package capstone.be.domain.diary.dto.request;
import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryDto;
import capstone.be.domain.hashtag.dto.HashtagDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryRequest{
    Long userId;
    String title;
    String weather;
    @JsonProperty("hashtag")
    Set<String> hashtagNames;
    String mood;

    String font;
    List<BProperties> blocks;


    public static DiaryRequest of(Long userId,String title, String weather, Set<String> hashtagNames, String mood, String font, List<BProperties> blocks) {
        return new DiaryRequest(userId,title, weather, hashtagNames, mood, font, blocks);
    }

    public DiaryDto toDto(Long Id){
        return DiaryDto.of(Id,title, weather,
                hashtagNames.stream().map(HashtagDto::of).collect(Collectors.toUnmodifiableSet())
                ,mood
                ,font
                ,blocks
        );
    }

    /*
    public static DiaryRequest of(String title, String weather, Set<String> hashtagNames, String mood, String font, List<BProperties> blocks) {
        return new DiaryRequest(title, weather, hashtagNames, mood, font, blocks);
    }

    public DiaryDto toDto(){
        return DiaryDto.of(title, weather,
                hashtagNames.stream().map(HashtagDto::of).collect(Collectors.toUnmodifiableSet())
                ,mood
                ,font
                ,blocks
        );
    }
    */



}
