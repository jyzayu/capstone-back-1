package capstone.be.global.advice;

import capstone.be.global.advice.exception.diary.CDiaryNotFoundException;
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


    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
