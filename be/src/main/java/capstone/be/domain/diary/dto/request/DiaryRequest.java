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
public class DiaryRequest {
    Long userId;
    String title;
    String weather;
    @JsonProperty("hashtag")
    Set<String> hashtagNames;
    String mood;
    String font;
    Integer likeCount;
    Integer viewCount;
    List<BProperties> blocks;

    public static DiaryRequest of(Long userId, String title, String weather, Set<String> hashtagNames, String mood, String font, List<BProperties> blocks, Integer likeCount, Integer viewCount) {
        return new DiaryRequest(userId, title, weather, hashtagNames, mood, font, likeCount, viewCount, blocks);
    }

    public DiaryDto toDto(Long Id) {
        return DiaryDto.of(Id, title, weather,
                hashtagNames.stream().map(HashtagDto::of).collect(Collectors.toUnmodifiableSet()),
                mood,
                font,
                null, // You can modify this line to pass thumbnail if available
                blocks,
                likeCount,
                viewCount
        );
    }
}
