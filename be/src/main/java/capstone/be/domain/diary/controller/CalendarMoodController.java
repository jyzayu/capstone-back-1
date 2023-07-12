package capstone.be.domain.diary.controller;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.response.CalendarResponse;
import capstone.be.domain.diary.dto.response.DiaryMoodSearchResponse;
import capstone.be.domain.diary.dto.response.DiaryMoodTotalResponse;
import capstone.be.domain.diary.service.DiaryService;
import capstone.be.global.advice.exception.calendar.CDiaryCalendarException;
import capstone.be.global.advice.exception.diary.CPageNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CalendarMoodController {

    private final DiaryService diaryService;

    //페이지형태
    @GetMapping("/mood")
    public List<DiaryMoodSearchResponse> getSortedDiariesByMood(@RequestParam(value = "mood", defaultValue = "best") String mood,
                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size){

        if(!"best".equals(mood) && !"good".equals(mood) && !"normal".equals(mood) && !"bad".equals(mood) && !"worst".equals(mood)){
            //지정된 기분 외 다른 기분인 일기일 때
            //에러코드 추가
            throw new CPageNotFoundException();
        }
        Page<Diary> sortedDiaries = diaryService.getSortedDiariesByMood(mood, page, size);

        //DiaryEntity를 dto로 변환
        List<Diary> diaryList = sortedDiaries.getContent();
        List<DiaryMoodSearchResponse> responses = diaryList.stream().map(DiaryMoodSearchResponse::from).collect(Collectors.toList());

        return responses;
    }

    //마이페이지 들어갈 때 전체 기분별 개수 보내주기
    @GetMapping("/mood/count")
    public DiaryMoodTotalResponse getTotalMood(){
        DiaryMoodTotalResponse response = diaryService.getMoodTotal();
        return response;
    }

    //캘린더 조회
    @GetMapping("/calendar")
    public List<CalendarResponse> getDiarySummariesByMonth(@RequestParam(value = "year", defaultValue = "#{T(java.time.LocalDate).now().getYear()}") int year,
                                                           @RequestParam(value = "month", defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") int month) {
        if (!(year > 1900) || !(year < 9999) || !(month >= 1) || !(month <= 12)){
            throw new CDiaryCalendarException(); //todo : int타입만 에러코드가 출력됨. 수정 필요.
        }

        //캘린더 정보 불러내기
        List<CalendarResponse> responseList = diaryService.getDiaryByMonth(year, month);

        return responseList;
    }
}
