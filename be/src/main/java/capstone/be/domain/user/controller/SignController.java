package capstone.be.domain.user.controller;

import capstone.be.domain.user.domain.User;
import capstone.be.domain.user.dto.KakaoProfile;
import capstone.be.domain.user.dto.LoginResponseDto;
import capstone.be.domain.user.repository.UserRepository;
import capstone.be.domain.user.service.KakaoService;
import capstone.be.domain.user.service.SignService;
import capstone.be.global.advice.exception.CEmailSignupFailedException;
import capstone.be.global.advice.exception.CNicknameSignupFailed2Exception;
import capstone.be.global.advice.exception.CNicknameSignupFailedException;
import capstone.be.global.advice.exception.CUserNotFoundException;
import capstone.be.global.dto.jwt.TokenDto;
import capstone.be.global.dto.jwt.TokenRequestDto;
import capstone.be.global.dto.response.ResponseService;
import capstone.be.global.dto.signup.UserLoginRequestDto;
import capstone.be.global.dto.signup.UserSignupRequestDto;
import capstone.be.global.dto.signup.UserSocialLoginRequestDto;
import capstone.be.global.dto.signup.UserSocialSignupRequestDto;
import capstone.be.global.jwt.JwtProvider;
import capstone.be.global.jwt.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SignController {

    private final SignService signService;
    private final KakaoService kakaoService;
    private final ResponseService responseService;
    private final JwtProvider jwtProvider;
    private final UserRepository userJpaRepo;

    private final RefreshTokenJpaRepository tokenJpaRepo;

    @PostMapping("/login")
    public HttpEntity<TokenDto> login(
            @RequestBody UserLoginRequestDto userLoginRequestDto) {

        TokenDto tokenDto = signService.login(userLoginRequestDto);
        return new HttpEntity<>(tokenDto);
    }

    @PostMapping("/signup")
    public HttpEntity<Long> signup(
            @RequestBody UserSignupRequestDto userSignupRequestDto) {
        Long signupId = signService.signup(userSignupRequestDto);
        return new HttpEntity<>(signupId);

    }

    @PostMapping("/reissue")
    public HttpEntity<TokenDto> reissue(HttpServletRequest request) {
        return new HttpEntity<>(signService.reissue(request));
    }

// Todo: code 프론트에서 보내주는 것 사용하게 되면  UserSocialLoginRequestDto및 카카오토큰 요청 코드 추가하기
    @PostMapping("/auth/login")
    public HttpEntity<LoginResponseDto> loginByKakao(
            @RequestBody UserSocialLoginRequestDto socialLoginRequestDto) {

        String kakaoAccessToken = socialLoginRequestDto.getAccessToken();
        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(kakaoAccessToken);
        if (kakaoProfile == null) throw new CUserNotFoundException();

        Optional<User> user = userJpaRepo.findById(kakaoProfile.getId());
        //신규 사용자인 경우 카카오토큰과 true
        if(user.isEmpty()){
            return new HttpEntity<>(
                    new LoginResponseDto(kakaoAccessToken, true));
        }
        else {
            //기존 사용자의 경우 JWT토큰과 false
            return new HttpEntity<>(
                    new LoginResponseDto(
                            jwtProvider.createTokenDto(user.get().getUserId(), Collections.singletonList("ROLE_USER")).getAccessToken(),
                            false)
            );
        }
    }


    // Todo : 에러 2,3,4 경우 카카오 계정이 1개이고 , 이메일 형식이 없어 테스트 못 함
    @PostMapping("/auth/signup")
    public HttpEntity<TokenDto> signupBySocial(
            @RequestBody UserSocialSignupRequestDto socialSignupRequestDto) {

        KakaoProfile kakaoProfile =
                kakaoService.getKakaoProfile(socialSignupRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new CUserNotFoundException();
        // 이미 가입된 이메일 002
        if(userJpaRepo.findByEmail(socialSignupRequestDto.getEmail()).isPresent()){
            throw new CEmailSignupFailedException();
        }
        //이미 가입된 닉네임 003
        if (userJpaRepo.findByNickname(socialSignupRequestDto.getNickname()).isPresent()) {
            throw new CNicknameSignupFailedException();
        }
        //잘못된 이메일 형식 005
        if (socialSignupRequestDto.getNickname().length() > 20){
            throw new CNicknameSignupFailed2Exception();
        }
        //기가입자 에러 006  signup() 내부에
        Long userId = signService.socialSignup(UserSignupRequestDto.builder()
                .userId(kakaoProfile.getId())
                .email(socialSignupRequestDto.getEmail())
                .nickname(socialSignupRequestDto.getNickname())
                .build());

        System.out.println(userId);
        return new HttpEntity<>(jwtProvider.createTokenDto(userId, Collections.singletonList("ROLE_USER")));
    }

//} 회원가입을 하려면 사용자 정보 email을 불러와서 id로 사용하여 repo에 저장해야하는데
    // 이는 token이 있어야 가능하다. 토큰으로 사용자 정보 불러오는데 리다이렉트에서 코드 받아서 토큰 받고 전달하고 끝내면 다시 회원가입 요청해야함
//    토큰을 받았으면 바로 다시 회원가입또는 로그인 요청?

}
