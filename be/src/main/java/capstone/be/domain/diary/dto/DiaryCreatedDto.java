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


public record DiaryCreatedDto(
        Long userId,
        String title,
        String date,
        String weather,
        Set<String> hashtag,
        String mood,

        String font,
        List<BProperties> blocks

){


    public Diary toEntity(){
        return Diary.of(userId,title, weather, mood, font, blocks);

    }


    @JsonCreator
    public static DiaryCreatedDto from(Diary diary){

        return new DiaryCreatedDto(diary.getUserId(), diary.getTitle(),
                diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                diary.getWeather(),
                diary.getHashtags().stream().map(Hashtag::getHashtagName).collect(Collectors.toUnmodifiableSet()),
                diary.getMood(),
                diary.getFont(),
                diary.getBlocks())
                ;
    }

    /*
    public Diary toEntity(){
        return Diary.of(title, weather, mood, font, blocks);

    }
    @JsonCreator
    public static DiaryCreatedDto from(Diary diary){

        return new DiaryCreatedDto(diary.getTitle(),
                diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                diary.getWeather(),
                diary.getHashtags().stream().map(Hashtag::getHashtagName).collect(Collectors.toUnmodifiableSet()),
                diary.getMood(),
                diary.getFont(),
                diary.getBlocks())
                ;
    }*/

}

