package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.hashtag.domain.Hashtag;
import capstone.be.domain.hashtag.dto.HashtagDto;
import com.fasterxml.jackson.annotation.JsonCreator;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.LocalDateTime;
import capstone.be.domain.hashtag.dto.HashtagDto;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record DiaryCreatedDto(
        Long userId,
        String title,
        String date,
        String weather,
        Set<String> hashtag,
        String mood,
        String font,
        Integer likeCount,
        Integer viewCount,
        List<BProperties> blocks
) {

    public Diary toEntity() {
        return Diary.of(userId, title, weather, mood, font, likeCount, viewCount, blocks);
    }

    @JsonCreator
    public static DiaryCreatedDto from(Diary diary) {
        return new DiaryCreatedDto(
                diary.getUserId(),
                diary.getTitle(),
                diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                diary.getWeather(),
                diary.getHashtags().stream().map(Hashtag::getHashtagName).collect(Collectors.toUnmodifiableSet()),
                diary.getMood(),
                diary.getFont(),
                diary.getLikeCount(),
                diary.getViewCount(),
                diary.getBlocks()
        );
    }
}
