package capstone.be.domain.diary.controller;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.response.*;
import capstone.be.domain.diary.service.DiaryService;
import capstone.be.global.advice.exception.calendar.CDiaryCalendarException;
import capstone.be.global.advice.exception.diary.CDiarySearchPageInvalidException;
import capstone.be.global.advice.exception.diary.CPageNotFoundException;
import capstone.be.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CalendarMoodController {

    private final DiaryService diaryService;
    private final JwtProvider jwtProvider;

    //페이지형태
    @GetMapping("/mood")
    public List<DiaryMoodSearchResponse> getSortedDiariesByMood(@RequestParam(value = "mood", defaultValue = "best") String mood,
                                                                @RequestParam(value = "page", defaultValue = "0") String page,
                                                                @RequestParam(value = "size", defaultValue = "10") String size,
                                                                HttpServletRequest tokenRequest
                                                                ){

        if(!"best".equals(mood) && !"good".equals(mood) && !"normal".equals(mood) && !"bad".equals(mood) && !"worst".equals(mood)){
            //지정된 기분 외 다른 기분인 일기일 때
            //에러코드 추가
            throw new CPageNotFoundException();
        }

        int intPage;
        int intSize;

        try { //정수로 바꿀 수 없는 형태로 입력 시
            intPage = Integer.parseInt(page);
            intSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new CPageNotFoundException();
        }

        String accessToken = jwtProvider.resolveToken(tokenRequest);
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));
        Page<Diary> sortedDiaries = diaryService.getSortedDiariesByMood(mood, intPage, intSize, userId);

        //DiaryEntity를 dto로 변환
        List<Diary> diaryList = sortedDiaries.getContent();
        List<DiaryMoodSearchResponse> responses = diaryList.stream().map(DiaryMoodSearchResponse::from).collect(Collectors.toList());

        return responses;
    }

    //마이페이지 들어갈 때 전체 기분별 개수 보내주기
    @GetMapping("/mood/count")
    public DiaryMoodTotalResponse getTotalMood(HttpServletRequest tokenRequest){
        String accessToken = jwtProvider.resolveToken(tokenRequest);
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));
        DiaryMoodTotalResponse response = diaryService.getMoodTotal(userId);
        return response;
    }

    //캘린더 조회
    @GetMapping("/calendar")
    public List<CalendarResponse> getDiarySummariesByMonth(
            @RequestParam(value = "year", defaultValue = "#{T(java.time.LocalDate).now().getYear()}") String yearStr,
            @RequestParam(value = "month", defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") String monthStr,
            HttpServletRequest tokenRequest) {

        int year;
        int month;

        try { //정수로 바꿀 수 없는 형태로 입력 시
            year = Integer.parseInt(yearStr);
            month = Integer.parseInt(monthStr);
        } catch (NumberFormatException e) {
            throw new CDiaryCalendarException();
        }

        if (!(year > 1900) || !(year < 9999) || !(month >= 1) || !(month <= 12)){ //정수 범위 초과 시
            throw new CDiaryCalendarException();
        }

        String accessToken = jwtProvider.resolveToken(tokenRequest);
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));

        //캘린더 정보 불러내기
        List<CalendarResponse> responseList = diaryService.getDiaryByMonth(year, month, userId);

        return responseList;
    }


    @GetMapping("/search")
    public DiaryPageResponse getSearchDiaryContents(@RequestParam(value = "text", defaultValue = "d") String text,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                    HttpServletRequest tokenRequest){
        boolean lastPage =false;

        String accessToken = jwtProvider.resolveToken(tokenRequest);

        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));

        Page<Diary> sortedDiaries = diaryService.getSearchDiaryTitle(text, page, size,userId);



        //DiaryEntity를 dto로 변환
        List<Diary> diaryList = sortedDiaries.getContent();

        
        int diaryNum= sortedDiaries.getTotalPages()-1;
        if(diaryNum == -1)
            diaryNum =0;

        //DIARY_012
        if (diaryNum>size)
            throw new CDiarySearchPageInvalidException();



        if(diaryNum == page)
            lastPage = true;
        else
            lastPage = false;
        List<DiaryContentSearchResponse> responses = diaryList.stream().map(DiaryContentSearchResponse::from).collect(Collectors.toList());
        DiaryPageResponse responses2 = DiaryPageResponse.from(responses,page,lastPage);
        return responses2;
    }
}
