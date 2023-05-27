package capstone.be.domain.diary.service;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryCreatedDto;
import capstone.be.domain.diary.dto.DiaryDto;
import capstone.be.domain.diary.dto.response.DiaryMoodTotalResponse;
import capstone.be.domain.hashtag.dto.HashtagDto;
import capstone.be.domain.diary.dto.response.DiaryCreateResponse;
import capstone.be.domain.diary.repository.DiaryRepository;
import capstone.be.domain.hashtag.domain.Hashtag;
import capstone.be.domain.hashtag.service.HashtagService;
import capstone.be.global.advice.exception.diary.CDiaryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final EntityManager entityManager;
    private final HashtagService hashtagService;

    public DiaryCreateResponse saveDiary(DiaryDto diaryDto){
        Diary diary = diaryDto.toEntity();
        diary.addHashtags(diaryDto.getHashtag().stream()
                .map(HashtagDto::toEntity)
                .collect(Collectors.toUnmodifiableSet()));


        return new DiaryCreateResponse(diaryRepository.save(diary).getId());
    }

    @Transactional(readOnly = true)
    public DiaryCreatedDto getDiary(Long diaryId){
        return diaryRepository.findById(diaryId)
                .map(DiaryCreatedDto::from)
                .orElseThrow(() -> new CDiaryNotFoundException());
    }

    public void updateDiary(Long diaryId, DiaryDto dto){
        try {
            Diary diary = diaryRepository.getReferenceById(diaryId);


            if (dto.getTitle() != null) { diary.setTitle(dto.getTitle()); }
            if (dto.getWeather() != null) { diary.setWeather(dto.getWeather()); }
            if (dto.getMood() != null) { diary.setMood(dto.getMood()); }

            Set<Long> hashtagIds = diary.getHashtags().stream()
                    .map(Hashtag::getId)
                    .collect(Collectors.toUnmodifiableSet());
            diary.clearHashtags();
            diaryRepository.flush();

            hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);

            diary.addHashtags(dto.getHashtag().stream().map(HashtagDto::toEntity).collect(Collectors.toUnmodifiableSet()));
        }catch (EntityNotFoundException e){
            // Diary_009
            throw new CDiaryNotFoundException();
        }
    }

    public void deleteDiary(Long diaryId) {
        Diary diary = diaryRepository.getReferenceById(diaryId);
        Set<Long> hashtagIds = diary.getHashtags().stream()
                .map(Hashtag::getId)
                .collect(Collectors.toUnmodifiableSet());

        diaryRepository.deleteById(diaryId);
        diaryRepository.flush();

        hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);
    }

    //다이어리 기분별 서치
    public Page<Diary> getSortedDiariesByMood(String mood, int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Diary> postList = diaryRepository.findByMood(mood, pageable);
        return postList;
    }

    //마이페이지 들어갈 때 전체 기분별 다이어리 개수 보내주기
    public DiaryMoodTotalResponse getMoodTotal() {
        String sql = "SELECT " +
                "(SELECT COALESCE(COUNT(*), 0) FROM Diary WHERE mood = 'best') AS bestCount, " +
                "(SELECT COALESCE(COUNT(*), 0) FROM Diary WHERE mood = 'good') AS goodCount, " +
                "(SELECT COALESCE(COUNT(*), 0) FROM Diary WHERE mood = 'normal') AS normalCount, " +
                "(SELECT COALESCE(COUNT(*), 0) FROM Diary WHERE mood = 'bad') AS badCount, " +
                "(SELECT COALESCE(COUNT(*), 0) FROM Diary WHERE mood = 'worst') AS worstCount ";

        Query query = entityManager.createNativeQuery(sql);

        Object[] result = (Object[]) query.getSingleResult();

        Long best = ((BigInteger) result[0]).longValue();
        Long good = ((BigInteger) result[1]).longValue();
        Long normal = ((BigInteger) result[2]).longValue();
        Long bad = ((BigInteger) result[3]).longValue();
        Long worst = ((BigInteger) result[4]).longValue();

        return new DiaryMoodTotalResponse(best, good, normal, bad, worst);
    }
}


