

package capstone.be.global.advice;

import capstone.be.global.advice.exception.CUserNotFound2Exception;
import capstone.be.global.advice.exception.security.*;
import capstone.be.global.dto.response.CommonResult;
import capstone.be.global.dto.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    /***
     * -9999
     * default Exception
     //     */
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected ResponseEntity<CommonResult> defaultException(HttpServletRequest request, Exception e) {
//        log.info(String.valueOf(e));
//
//        return new ResponseEntity<>(responseService.getFailResult(
//                (getMessage("unKnown.code"))));
//    }

    /***
     * -1000
     * 유저를 찾지 못했을 때 발생시키는 예외
     */
    @ExceptionHandler(CUserNotFoundException.class)
    protected ResponseEntity<CommonResult> userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.getFailResult(
                (getMessage("userNotFound.code"))
        ));
    }



    /***
     * AUTH_001
     * Social 인증 과정에서 문제 발생하는 에러  잘못된 카카오 액세스토큰 ,등등
     */
    @ExceptionHandler(CCommunicationException.class)
    protected ResponseEntity<CommonResult> communicationException(HttpServletRequest request, CCommunicationException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseService.getFailResult(
                (getMessage("communicationException.code"))
        ));
    }

    /***
     *  AUTH_002
     * 회원 가입 시 이미 로그인 된 이메일인 경우 발생 시키는 예외
     */
    @ExceptionHandler(CEmailSignupFailedException.class)
    protected ResponseEntity<CommonResult> emailSignupFailedException(HttpServletRequest request, CEmailSignupFailedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.getFailResult(
                (getMessage("emailSignupFailed.code"))
        ));
    }

    /**
     * AUTH_003
     * 이미 가입된 닉네임
     */
    @ExceptionHandler(CNicknameSignupFailedException.class)
    protected ResponseEntity<CommonResult> nicknameSignupFailedException(HttpServletRequest request, CNicknameSignupFailedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseService.getFailResult(
                (getMessage("nicknameSignupFailed.code"))
        ));
    }

    /**
     잘못된 닉네임 형식 AUTH_005
     */
    @ExceptionHandler(CNicknameSignupFailed2Exception.class)
    protected ResponseEntity<CommonResult> nicknameSignupFailed2Exception(HttpServletRequest request, CNicknameSignupFailed2Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseService.getFailResult(
                (getMessage("wrongNickNameFailed.code"))
        ));
    }

    /***
     * -AUTH_006
     * 기 가입자 에러
     */
    @ExceptionHandler(CUserExistException.class)
    protected ResponseEntity<CommonResult> existUserException(HttpServletRequest request, CUserExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseService.getFailResult(
                (getMessage("userExistException.code"))
        ));
    }


    /**
     * AUTH_008
     * 액세스 토큰 만료시 발생하는 에러
     */
    @ExceptionHandler(CLogoutTokenException.class)
    protected ResponseEntity<CommonResult> logoutAccessTokenException(HttpServletRequest request, CLogoutTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseService.getFailResult(
                (getMessage("accessTokenInValid.code"))
        ));
    }


    /**
     * AUTH_009
     * refresh token 에러시 발생 시키는 에러
     */
    @ExceptionHandler(CRefreshTokenException.class)
    protected ResponseEntity<CommonResult> refreshTokenException(HttpServletRequest request, CRefreshTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseService.getFailResult(
                (getMessage("refreshTokenInValid.code"))
        ));
    }


    /**
     * -1003
     * 전달한 Jwt 이 정상적이지 않은 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    protected ResponseEntity<CommonResult> authenticationEntrypointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseService.getFailResult(
                (getMessage("authenticationEntrypoint.code"))
        ));
    }

    /**
     * -1004
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAccessDeniedException.class)
    protected ResponseEntity<CommonResult> accessDeniedException(HttpServletRequest request, CAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseService.getFailResult(
                (getMessage("accessDenied.code"))
        ));
    }


    /***
     * AUTH 010
     * 유저를 찾을 수 없을 때 발생하는 에러
     */
    @ExceptionHandler(CUserNotFound2Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<CommonResult> userNotFound2Exception(HttpServletRequest request, Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.getFailResult(
                (getMessage("userNotFound2.code"))));
    }


    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}