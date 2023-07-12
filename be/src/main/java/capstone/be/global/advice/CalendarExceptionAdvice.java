package capstone.be.global.advice;

import capstone.be.global.advice.exception.calendar.CDiaryCalendarException;
import capstone.be.global.advice.exception.s3.S3ImgFormatException;
import capstone.be.global.dto.response.CommonResult;
import capstone.be.global.dto.response.ResponseService;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CalendarExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;


    //CALENDAR_001
    @ExceptionHandler(CDiaryCalendarException.class)
    protected ResponseEntity<CommonResult> PageNotFoundException(HttpServletRequest request, CDiaryCalendarException e) {
        return ResponseEntity.status(400).body(responseService.getFailResult(
                (getMessage("noDiaryCalendarException.code"))
        ));
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}