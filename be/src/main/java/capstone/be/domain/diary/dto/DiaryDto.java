package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.hashtag.dto.HashtagDto;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;
import java.util.stream.Stream;


import lombok.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryDto {
        Long userId;
        String title;
        String weather;
        Set<HashtagDto> hashtag;
        String mood;
        String font;
        String thumbnail;
        List<BProperties> blocks;
        Integer likeCount;
        Integer viewCount;

        public Diary toEntity() {
                return Diary.of(userId, title, weather, mood, font, thumbnail, likeCount, viewCount, blocks);
        }

        public static DiaryDto from(Diary diary) {
                return new DiaryDto(
                        diary.getUserId(),
                        diary.getTitle(),
                        diary.getWeather(),
                        diary.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toUnmodifiableSet()),
                        diary.getMood(),
                        diary.getFont(),
                        diary.getThumbnail(),
                        diary.getBlocks(),
                        diary.getLikeCount(),
                        diary.getViewCount()
                        );
        }

        public static DiaryDto of(Long userId, String title, String weather, Set<HashtagDto> hashtag, String mood, String font, List<BProperties> blocks, Integer likeCount, Integer viewCount) {
                return new DiaryDto(userId, title, weather, hashtag, mood, font, null, blocks, likeCount, viewCount);
        }

        public static DiaryDto of(Long userId, String title, String weather, Set<HashtagDto> hashtag, String mood, String font, String thumbnail, List<BProperties> blocks, Integer likeCount, Integer viewCount) {
                return new DiaryDto(userId, title, weather, hashtag, mood, font, thumbnail,  blocks,likeCount, viewCount);
        }
}

        /*
        public Diary toEntity(){
                return Diary.of(title, weather, mood, font, thumbnail, blocks);
        }

        public static DiaryDto from(Diary diary){
                return new DiaryDto( diary.getTitle(), diary.getWeather(),
                        diary.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toUnmodifiableSet()),
                        diary.getMood(),
                        diary.getFont(),
                        diary.getThumbnail(),
                        diary.getBlocks());
        }

        public static DiaryDto of(String title, String weather, Set<HashtagDto> hashtag, String mood,String font, List<BProperties> blocks) {
                return new DiaryDto(title, weather, hashtag, mood, font,null, blocks);
        }

        public static DiaryDto of(String title, String weather, Set<HashtagDto> hashtag, String mood,String font, String thumbnail, List<BProperties> blocks) {
                return new DiaryDto(title, weather, hashtag, mood, font,thumbnail, blocks);
        }*/

