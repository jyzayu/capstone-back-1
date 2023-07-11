

package capstone.be.domain.diary.controller;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryCreatedDto;
import capstone.be.domain.diary.dto.request.DiaryRequest;
import capstone.be.domain.diary.dto.response.CalendarResponse;
import capstone.be.domain.diary.dto.response.DiaryCreateResponse;
import capstone.be.domain.diary.dto.response.DiaryMoodSearchResponse;
import capstone.be.domain.diary.dto.response.DiaryMoodTotalResponse;
import capstone.be.domain.diary.service.DiaryService;
import capstone.be.global.advice.exception.calendar.CDiaryCalendarException;
import capstone.be.global.advice.exception.diary.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<DiaryCreateResponse> createDiary(@RequestBody DiaryRequest diaryRequest) throws IOException{   // id 만 반환하는 응답
        Optional<BProperties> levelConfirm = diaryRequest.getBlocks().stream().filter(x -> x.getData().getLevel()>=4).findAny();

        Optional<BProperties> sortConfirm = diaryRequest.getBlocks().stream().filter(x -> !x.getData().getAlign().equals("left")).findAny();

        Optional<BProperties> blockTypeConfirm = diaryRequest.getBlocks().stream()
                .filter(x ->  !x.getType().equals("text")
                        && !x.getType().equals("img")
                        && !x.getType().equals("heading")).findAny();

        //DIARY_007
        if(levelConfirm.isPresent()){
            throw new CDiaryOverLevelException();
        }

        //DIARY_005
        if(sortConfirm.isPresent()){
            throw new CDiaryInvalidSortException();
        }

        //DIARY_004
        if(blockTypeConfirm.isPresent()){
            throw new CDiaryInvalidBlockException();
        }

        //DIARY_002
        if (diaryRequest.getMood().equals("best") ||
                diaryRequest.getMood().equals("good") ||
                diaryRequest.getMood().equals("normal") ||
                diaryRequest.getMood().equals("bad") ||
                diaryRequest.getMood().equals("worst") ){
        }
        else {
            throw new CDiaryInvalidMoodException();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.save(diaryRequest.toDto()));
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryCreatedDto> diary(@PathVariable Long diaryId){   // DiaryDto에 date(createdAt)이 추가된 응답
        return ResponseEntity.ok(diaryService.getDiary(diaryId));
    }


    @PatchMapping("/{diaryId}")
    public ResponseEntity<?> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryRequest diaryRequest){
        diaryService.updateDiary(diaryId, diaryRequest.toDto());
        return ResponseEntity.ok("");
    }


    @DeleteMapping("/{diaryId}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long diaryId){  // 응답 값 없음
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

}
