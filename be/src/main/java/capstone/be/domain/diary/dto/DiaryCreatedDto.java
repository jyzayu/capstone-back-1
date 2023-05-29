package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.hashtag.dto.HashtagDto;
import com.fasterxml.jackson.annotation.JsonCreator;

import com.fasterxml.jackson.annotation.JsonCreator;

import capstone.be.domain.hashtag.dto.HashtagDto;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;


public record DiaryCreatedDto(
        String title,
        String weather,
        Set<HashtagDto> hashtag,
        String mood,

        String font,

        List<BProperties> blocks,
        LocalDateTime date
){

    public Diary toEntity(){
        return Diary.of(title, weather, mood, font, blocks);
    }

    @JsonCreator
    public static DiaryCreatedDto from(Diary diary){
        return new DiaryCreatedDto(diary.getTitle(), diary.getWeather(),
                diary.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toUnmodifiableSet()),
                diary.getMood(),
                diary.getFont(),
                diary.getBlocks(),
                diary.getCreatedAt());

    }

    }

