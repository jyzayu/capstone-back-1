package capstone.be.global.advice;

import capstone.be.global.advice.exception.diary.*;
import capstone.be.global.advice.exception.security.CLogoutTokenException;
import capstone.be.global.dto.response.CommonResult;
import capstone.be.global.dto.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class DiaryExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    //Diary_001
    @ExceptionHandler(CDiaryOverHashtagException.class)
    protected ResponseEntity<CommonResult> DiaryOverHashtagException(HttpServletRequest request, CDiaryOverHashtagException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("hashtagException.code"))
        ));
    }

    //DIARY_002
    @ExceptionHandler(CDiaryInvalidMoodException.class)
    protected ResponseEntity<CommonResult> DiaryInvalidMoodException(HttpServletRequest request, CDiaryInvalidMoodException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("moodException.code"))
        ));
    }

    //DIARY_004
    @ExceptionHandler(CDiaryInvalidBlockException.class)
    protected ResponseEntity<CommonResult> DiaryInvalidBlockException(HttpServletRequest request, CDiaryInvalidBlockException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("typeException.code"))
        ));
    }

    //DIARY_005
    @ExceptionHandler(CDiaryInvalidSortException.class)
    protected ResponseEntity<CommonResult> DiaryInvalidSortException(HttpServletRequest request, CDiaryInvalidSortException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("sortException.code"))
        ));
    }

    //DIARY_006 : 폰트 에러
    @ExceptionHandler(CDiaryInvalidFontException.class)
    protected ResponseEntity<CommonResult> CDiaryInvalidFontException(HttpServletRequest request, CDiaryInvalidFontException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("fontException.code"))
        ));
    }

    //DIARY_007
    @ExceptionHandler(CDiaryOverLevelException.class)
    protected ResponseEntity<CommonResult> DiaryOverLevelException(HttpServletRequest request, CDiaryOverLevelException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("levelException.code"))
        ));
    }

    //DIARY_008
    @ExceptionHandler(CDiaryNotFoundException.class)
    protected ResponseEntity<CommonResult> DiaryNotFoundException(HttpServletRequest request, CDiaryNotFoundException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("noDiaryException.code"))
        ));
    }

    //DIARY_009
    @ExceptionHandler(CDiaryPastEditException.class)
    protected ResponseEntity<CommonResult> CDiaryEditException(HttpServletRequest request, CDiaryPastEditException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("pastEditException.code"))
        ));
    }

    //DIARY_011
    @ExceptionHandler(CPageNotFoundException.class)
    protected ResponseEntity<CommonResult> PageNotFoundException(HttpServletRequest request, CPageNotFoundException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("noPageException.code"))
        ));
    }
    //DIARY_012
    @ExceptionHandler(CDiarySearchPageInvalidException.class)
    protected ResponseEntity<CommonResult> CDiarySearchPageInvalidException(HttpServletRequest request, CDiarySearchPageInvalidException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("noSearchPageException.code"))
        ));
    }
    //DIARY_013
    @ExceptionHandler(CMoreNewDiaryException.class)
    protected ResponseEntity<CommonResult> CMoreNewDiaryException(HttpServletRequest request, CMoreNewDiaryException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("newDiaryException.code"))
        ));
    }

    @ExceptionHandler(CSearchLogNotFoundException.class)
    protected ResponseEntity<CommonResult> CSearchLogNotFoundException(HttpServletRequest request, CSearchLogNotFoundException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                 (getMessage("SearchLogNotFoundException.code"))
        ));
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
