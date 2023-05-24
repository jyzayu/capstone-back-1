

package capstone.be.domain.diary.controller;

import capstone.be.domain.diary.dto.DiaryCreatedDto;
import capstone.be.domain.diary.dto.DiaryDto;
import capstone.be.domain.diary.dto.request.DiaryRequest;
import capstone.be.domain.diary.dto.response.DiaryCreateResponse;
import capstone.be.domain.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<DiaryCreateResponse> createDiary(@RequestBody DiaryRequest diaryRequest){   // id 만 반환하는 응답
        return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.saveDiary(diaryRequest.toDto()));
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
