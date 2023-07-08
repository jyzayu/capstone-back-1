package capstone.be.global.advice;

import capstone.be.global.advice.exception.diary.*;
import capstone.be.global.advice.exception.security.CLogoutTokenException;
import capstone.be.global.dto.response.CommonResult;
import capstone.be.global.dto.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
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

    //DIARY_009
    @ExceptionHandler(CDiaryNotFoundException.class)
    protected ResponseEntity<CommonResult> DiaryNotFoundException(HttpServletRequest request, CDiaryNotFoundException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("noDiaryException.code"))
        ));
    }

    //DIARY_008
    @ExceptionHandler(CDiaryNotFoundLinkException.class)
    protected ResponseEntity<CommonResult> DiaryNotFoundLinkException(HttpServletRequest request, CDiaryNotFoundLinkException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("linkException.code"))
        ));
    }
    //DIARY_007
    @ExceptionHandler(CDiaryOverLevelException.class)
    protected ResponseEntity<CommonResult> DiaryOverLevelException(HttpServletRequest request, CDiaryOverLevelException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("levelException.code"))
        ));
    }

    //DIARY_004
    @ExceptionHandler(CDiaryInvalidSortException.class)
    protected ResponseEntity<CommonResult> DiaryInvalidSortException(HttpServletRequest request, CDiaryInvalidSortException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("sortException.code"))
        ));
    }

    //DIARY_003
    @ExceptionHandler(CDiaryInvalidBlockException.class)
    protected ResponseEntity<CommonResult> DiaryInvalidBlockException(HttpServletRequest request, CDiaryInvalidBlockException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("typeException.code"))
        ));
    }

    //DIARY_002
    @ExceptionHandler(CDiaryInvalidMoodException.class)
    protected ResponseEntity<CommonResult> DiaryInvalidMoodException(HttpServletRequest request, CDiaryInvalidMoodException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("moodException.code"))
        ));
    }

    //DIARY_006
    @ExceptionHandler(CDiaryNotExistException.class)
    protected ResponseEntity<CommonResult> DiaryNotExistException(HttpServletRequest request, CDiaryNotExistException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("linkException.code"))
        ));
    }

    //Diary_001
    @ExceptionHandler(CDiaryOverHashtagException.class)
    protected ResponseEntity<CommonResult> DiaryOverHashtagException(HttpServletRequest request, CDiaryOverHashtagException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("hashtagException.code"))
        ));
    }


    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
