package capstone.be.global.advice;

import capstone.be.global.advice.exception.*;
import capstone.be.global.dto.response.CommonResult;
import capstone.be.global.dto.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
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
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected HttpEntity<CommonResult> defaultException(HttpServletRequest request, Exception e) {
        log.info(String.valueOf(e));

        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("unKnown.code"))));
    }

    /***
     * -1000
     * 유저를 찾지 못했을 때 발생시키는 예외
     */
    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected HttpEntity<CommonResult> userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("userNotFound.code"))
        ));
    }

    /***
     * -1001
     * 유저 이메일 로그인 실패 시 발생시키는 예외
     */
    @ExceptionHandler(CEmailLoginFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected HttpEntity<CommonResult> emailLoginFailedException(HttpServletRequest request, CEmailLoginFailedException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("emailLoginFailed.code"))
        ));
    }

    /***
     * AUTH_001  1007
     * Social 인증 과정에서 문제 발생하는 에러  잘못된 카카오 액세스토큰 ,등등
     */
    @ExceptionHandler(capstone.be.global.advice.exception.CCommunicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected HttpEntity<CommonResult> communicationException(HttpServletRequest request, CCommunicationException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("communicationException.code"))
        ));
    }

    /***
     * -1002 AUTH_002
     * 회원 가입 시 이미 로그인 된 이메일인 경우 발생 시키는 예외
     */
    @ExceptionHandler(CEmailSignupFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected HttpEntity<CommonResult> emailSignupFailedException(HttpServletRequest request, CEmailSignupFailedException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("emailSignupFailed.code"))
        ));
    }

    /**
     * AUTH_003
     * 이미 가입된 닉네임
     */
    @ExceptionHandler(CNicknameSignupFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected HttpEntity<CommonResult> nicknameSignupFailedException(HttpServletRequest request, CNicknameSignupFailedException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("nicknameSignupFailed.code"))
        ));
    }

    /**
    잘못된 닉네임 형식 AUTH_005
    */
    @ExceptionHandler(CNicknameSignupFailed2Exception.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected HttpEntity<CommonResult> nicknameSignupFailed2Exception(HttpServletRequest request, CNicknameSignupFailed2Exception e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("wrongNickNameFailed.code"))
        ));
    }

    /***
     * -AUTH_006
     * 기 가입자 에러
     */
    @ExceptionHandler(CUserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected HttpEntity<CommonResult> existUserException(HttpServletRequest request, CUserExistException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("userExistException.code"))
        ));
    }





    /**
     * -1003
     * 전달한 Jwt 이 정상적이지 않은 경우 발생 시키는 예외
     */
    @ExceptionHandler(capstone.be.global.advice.exception.CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected HttpEntity<CommonResult> authenticationEntrypointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("authenticationEntrypoint.code"))
        ));
    }

    /**
     * -1004
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     */
    @ExceptionHandler(capstone.be.global.advice.exception.CAccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected HttpEntity<CommonResult> accessDeniedException(HttpServletRequest request, CAccessDeniedException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("accessDenied.code"))
        ));
    }

    /**
     * -1005
     * refresh token 에러시 발생 시키는 에러
     */
    @ExceptionHandler(CRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected HttpEntity<CommonResult> refreshTokenException(HttpServletRequest request, CRefreshTokenException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("refreshTokenInValid.code"))
        ));
    }

    /**
     * -1006
     * 액세스 토큰 만료시 발생하는 에러
     */
    @ExceptionHandler(CExpiredAccessTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected HttpEntity<CommonResult> expiredAccessTokenException(HttpServletRequest request, CExpiredAccessTokenException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("expiredAccessToken.code"))
        ));
    }





    /***
     * -1009
     * 소셜 로그인 시 필수 동의항목 미동의시 에러
     */
    @ExceptionHandler(capstone.be.global.advice.exception.CSocialAgreementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected HttpEntity<CommonResult> socialAgreementException(HttpServletRequest request, CSocialAgreementException e) {
        return new HttpEntity<>(responseService.getFailResult(
                (getMessage("agreementException.code"))));
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}