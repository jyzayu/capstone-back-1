package capstone.be.domain.diary.service;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryRandomDto;
import capstone.be.domain.diary.dto.response.DiaryMainTotalResponse;
import capstone.be.domain.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.TemporalAdjusters;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MainService {
    private final DiaryRepository diaryRepository;

    public DiaryRandomDto getRandomDiary(Long userId){
        List<Diary> dto = diaryRepository.findRandom(userId);




//        System.out.println(idx);
        Diary diary = null;

//        Page<Diary> diaryPage = diaryRepository.findAll(PageRequest.of(idx, 1));
        if(dto.isEmpty()){
//            diary = diaryPage.getContent().get(0);
            return null;
        }
        else{
            diary = dto.get(0);
        }
        return DiaryRandomDto.from(diary);
    }

    public DiaryMainTotalResponse getDiaryTotal(Long userId){
        // 현재 시스템의 날짜를 가져옴
        LocalDate today = LocalDate.now();
        
        // 현재 달의 첫 번째 날
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        
        // 현재 달의 마지막 날
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        // LocalDateTime으로 변환
        LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
        LocalDateTime endOfMonth = lastDayOfMonth.atTime(23, 59, 59);

        // 올해의 첫 번째 날
        LocalDate firstDayOfYear = today.withDayOfYear(1);

        // 올해의 마지막 날
        LocalDate lastDayOfYear = today.withDayOfYear(today.lengthOfYear());

        // LocalDateTime으로 변환
        LocalDateTime startOfYear = firstDayOfYear.atStartOfDay();
        LocalDateTime endOfYear = lastDayOfYear.atTime(23, 59, 59);

        Long ycnt = diaryRepository.countByUserIdAndCreatedAtBetween(userId, startOfYear, endOfYear);
        Long mcnt = diaryRepository.countByUserIdAndCreatedAtBetween(userId, startOfMonth, endOfMonth);

        return DiaryMainTotalResponse.of(ycnt, mcnt);
    }


}
