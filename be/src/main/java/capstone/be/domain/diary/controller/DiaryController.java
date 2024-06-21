

package capstone.be.domain.diary.controller;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryCreatedDto;
import capstone.be.domain.diary.dto.DiaryRandomDto;
import capstone.be.domain.diary.dto.PopularDto;
import capstone.be.domain.diary.dto.request.DiaryRequest;
import capstone.be.domain.diary.dto.response.DiaryCreateResponse;
import capstone.be.domain.diary.dto.response.DiaryMainTotalResponse;
import capstone.be.domain.diary.service.DiaryService;
import capstone.be.domain.diary.service.MainService;
import capstone.be.global.advice.exception.diary.*;
import capstone.be.global.jwt.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DiaryController {

    private final DiaryService diaryService;
    private final MainService mainService;
    private final JwtProvider jwtProvider;

    @PostMapping("/diary")
    public ResponseEntity<DiaryCreateResponse> createDiary(@RequestBody DiaryRequest diaryRequest, HttpServletRequest tokenRequest)
            throws IOException {   // id 만 반환하는 응답
        int hashCnt = 0;
        Long blockId = 0L;

        String accessToken = jwtProvider.resolveToken(tokenRequest);

        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));

        Optional<BProperties> levelConfirm = diaryRequest.getBlocks().stream().filter(x -> x.getData().getLevel() < 1 &&
                x.getData().getLevel() > 5).findAny();

        Optional<BProperties> sortConfirm = diaryRequest.getBlocks().stream().filter(x -> !x.getData().getAlign().equals("left")
                && !x.getData().getAlign().equals("center")
                && !x.getData().getAlign().equals("right")).findAny();


        for (int j = 0; j < diaryRequest.getBlocks().stream().count(); j++) {
            diaryRequest.getBlocks().get(j).setId(blockId++);
        }

        Optional<BProperties> blockTypeConfirm = diaryRequest.getBlocks().stream()
                .filter(x -> !x.getType().equals("text")
                        && !x.getType().equals("img")
                        && !x.getType().equals("heading")).findAny();

        for (String hashtagName : diaryRequest.getHashtagNames()) {
            hashCnt++;
        }

        //DIARY_007
        if (levelConfirm.isPresent()) {
            throw new CDiaryOverLevelException();
        }

        //DIARY_006
        if (!(diaryRequest.getFont().equals("basic") ||
                diaryRequest.getFont().equals("neo") ||
                diaryRequest.getFont().equals("namsan") ||
                diaryRequest.getFont().equals("maru") ||
                diaryRequest.getFont().equals("hyemin") ||
                diaryRequest.getFont().equals("diary") ||
                diaryRequest.getFont().equals("zziba"))) {
            throw new CDiaryInvalidFontException();
        }

        //DIARY_005
        if (sortConfirm.isPresent()) {
            throw new CDiaryInvalidSortException();
        }

        //DIARY_004
        if (blockTypeConfirm.isPresent()) {
            throw new CDiaryInvalidBlockException();
        }

        //DIARY_002
        if (diaryRequest.getMood().equals("best") ||
                diaryRequest.getMood().equals("good") ||
                diaryRequest.getMood().equals("normal") ||
                diaryRequest.getMood().equals("bad") ||
                diaryRequest.getMood().equals("worst")) {
        } else {
            throw new CDiaryInvalidMoodException();
        }

        //DIARY_001
        if (hashCnt > 5) {
            throw new CDiaryOverHashtagException();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.save(diaryRequest.toDto(userId)));
        //return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.save(diaryRequest.toDto()));

    }

    @GetMapping("/diary/{diaryId}")
    public ResponseEntity<DiaryCreatedDto> diary(@PathVariable Long diaryId,
                                                 HttpServletRequest tokenRequest) throws JsonProcessingException {   // DiaryDto에 date(createdAt)이 추가된 응답
        String accessToken = jwtProvider.resolveToken(tokenRequest);
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));
        return ResponseEntity.ok(diaryService.getDiary(diaryId, userId));
    }


    @PatchMapping("/diary/{diaryId}")
    public ResponseEntity<?> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryRequest diaryRequest,
                                         HttpServletRequest tokenRequest) {

        String accessToken = jwtProvider.resolveToken(tokenRequest);

        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));

        Long blockId = 0L;
        for (int j = 0; j < diaryRequest.getBlocks().stream().count(); j++) {
            diaryRequest.getBlocks().get(j).setId(blockId++);
        }
        diaryService.updateDiary(diaryId, diaryRequest.toDto(userId));
        //diaryService.updateDiary(diaryId, diaryRequest.toDto());
        return ResponseEntity.ok("");
    }


    @DeleteMapping("/diary/{diaryId}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long diaryId, HttpServletRequest tokenRequest) {  // 응답 값 없음
        String accessToken = jwtProvider.resolveToken(tokenRequest);

        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));

        diaryService.deleteDiary(diaryId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

    @GetMapping("/random/diary")
    public ResponseEntity<?> getRandomDiary(HttpServletRequest tokenRequest) {
        String accessToken = jwtProvider.resolveToken(tokenRequest);
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));


        DiaryRandomDto randomDiary = mainService.getRandomDiary(userId);
        if (randomDiary != null) {
            return ResponseEntity.ok(randomDiary);
        } else {
            return ResponseEntity.ok("{}");
        }
    }

    @GetMapping("/total")
    public ResponseEntity<DiaryMainTotalResponse> getMainTotal(HttpServletRequest tokenRequest) {
        String accessToken = jwtProvider.resolveToken(tokenRequest);
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));


        return ResponseEntity.ok(mainService.getDiaryTotal(userId));
    }

    @GetMapping("/diary/findAll")
    public List<DiaryCreatedDto> getTotalDiary(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size, HttpServletRequest tokenRequest) {

        String accessToken = jwtProvider.resolveToken(tokenRequest);

        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));

        Page<Diary> sortedDiaries = diaryService.getAllDiary(userId, page, size);

        List<Diary> diaries = sortedDiaries.getContent();

        List<DiaryCreatedDto> allDiary = diaries.stream().map(DiaryCreatedDto::from).collect(Collectors.toList());
        return allDiary;
    }

    @GetMapping("/healthy")
    public ResponseEntity<Void> checkHealthStatus() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularDto>> getPopular() {
        return ResponseEntity.ok(diaryService.getPopularDiaries());
    }


}
