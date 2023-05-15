package capstone.be.domain.diary.service;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryCreatedDto;
import capstone.be.domain.diary.dto.DiaryDto;
import capstone.be.domain.diary.dto.response.DiaryCreateResponse;
import capstone.be.domain.diary.repository.DiaryRepository;
import capstone.be.global.advice.exception.diary.CDiaryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public DiaryCreateResponse save(DiaryDto diaryDto){
        Diary diary = diaryDto.toEntity();
        return new DiaryCreateResponse(diaryRepository.save(diary).getId());
    }

    public DiaryCreatedDto getDiary(Long diaryId){
        return diaryRepository.findById(diaryId).map(DiaryCreatedDto::from).orElseThrow(() -> new CDiaryNotFoundException());
    }

    public void update(Long diaryId, DiaryDto dto){
        try {
            Diary diary = diaryRepository.getReferenceById(diaryId);
            if (dto.getTitle() != null) { diary.setTitle(dto.getTitle()); }
            if (dto.getWeather() != null) { diary.setWeather(dto.getWeather(); }
            if (dto.getHashtag() != null) { diary.setHashtag(dto.getHashtag()); }
            if (dto.getMood() != null) { diary.setMood(dto.getMood()); }
        }catch (EntityNotFoundException e){
            // Diary_009
            throw new CDiaryNotFoundException();
        }
    }

    public void delete(Long diaryId){
        diaryRepository.deleteById(diaryId);
    }
}


