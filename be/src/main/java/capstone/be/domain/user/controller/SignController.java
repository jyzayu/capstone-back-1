
package capstone.be.domain.user.controller;

import capstone.be.domain.user.domain.User;
import capstone.be.domain.user.dto.KakaoProfile;
import capstone.be.domain.user.dto.LoginResponseDto;
import capstone.be.domain.user.dto.UserInfoDto;
import capstone.be.domain.user.repository.UserRepository;
import capstone.be.domain.user.service.EditUserService;
import capstone.be.domain.user.service.KakaoService;
import capstone.be.domain.user.service.SignService;
import capstone.be.global.advice.exception.security.CEmailSignupFailedException;
import capstone.be.global.advice.exception.security.CNicknameSignupFailed2Exception;
import capstone.be.global.advice.exception.security.CNicknameSignupFailedException;
import capstone.be.global.advice.exception.security.CUserNotFoundException;
import capstone.be.global.dto.jwt.ReissueDto;
import capstone.be.global.dto.jwt.TokenDto;
import capstone.be.global.dto.response.ResponseService;
import capstone.be.global.dto.signup.UserSignupRequestDto;
import capstone.be.global.dto.signup.UserSocialLoginRequestDto;
import capstone.be.global.dto.signup.UserSocialSignupRequestDto;
import capstone.be.global.jwt.JwtProvider;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    private final RedisTemplate<String, String> redisTemplate;

    private final EditUserService editUserService;


    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        String accessToken = jwtProvider.resolveToken(request);

        //atk로 userId 얻기
        String userId = jwtProvider.getSubjects(accessToken);

        //엑세스 토큰 남은 유효시간
        Long expiration = jwtProvider.getExpiration(accessToken);

        //accessToken blackList 등록 expiration설정해서
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        // 저장된 rtk가 있다면 refreshToken삭제
        if (redisTemplate.opsForValue().get("RT:" + userId) != null){
            redisTemplate.delete("RT:" + userId);
        }

        return ResponseEntity.ok("");
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<ReissueDto> reissue(HttpServletRequest request) {
        return ResponseEntity.ok(signService.reissue(request));
    }

    // Todo: code 프론트에서 보내주는 것 사용하게 되면  UserSocialLoginRequestDto및 카카오토큰 요청 코드 추가하기
    @PostMapping("/auth/login")
    public ResponseEntity<?> loginByKakao(
            @RequestBody UserSocialLoginRequestDto socialLoginRequestDto) {

        String kakaoAccessToken = socialLoginRequestDto.getAccessToken();
        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(kakaoAccessToken);
        if (kakaoProfile == null) throw new CUserNotFoundException();

        Long userId = kakaoProfile.getId();
        Optional<User> user = userJpaRepo.findById(userId);
        //신규 사용자인 경우 카카오토큰과 true
        if(user.isEmpty()){
            return ResponseEntity.ok(
                    new LoginResponseDto(kakaoAccessToken, null, true));
        }
        else {
            TokenDto tokenDto = jwtProvider.createTokenDto(userId, Collections.singletonList("ROLE_USER"));

            //redis에 rtk 저장
            Long expiration = jwtProvider.getExpiration(tokenDto.getRefreshToken());
            redisTemplate.opsForValue().set("RT:" + userId,tokenDto.getRefreshToken(), expiration, TimeUnit.MILLISECONDS);

            //기존 사용자의 경우 JWT토큰과 false
            return ResponseEntity.ok(new LoginResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken(), false));
        }
    }


    // Todo : 에러 2,3,4 경우 카카오 계정이 1개이고 , 이메일 형식이 없어 테스트 못 함
    @PostMapping("/auth/signup")
    public ResponseEntity<TokenDto> signupBySocial(
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
        TokenDto tokenDto = jwtProvider.createTokenDto(userId, Collections.singletonList("ROLE_USER"));

        // redis 에 rtk 저장
        Long expiration = jwtProvider.getExpiration(tokenDto.getRefreshToken());
        redisTemplate.opsForValue().set("RT:" + userId,tokenDto.getRefreshToken(), expiration, TimeUnit.MILLISECONDS);


        return ResponseEntity.ok(tokenDto);
    }

    //Todo : 로그아웃 코드가 중복되는데 서비스로 옮겨서 사용할 수 있을까?
    //회원 탈퇴 -> 로그아웃 후 delete
    @DeleteMapping("/auth/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest request){
        String accessToken = jwtProvider.resolveToken(request);
        String userId = jwtProvider.getSubjects(accessToken);

        //로그아웃 선행
        Long expiration = jwtProvider.getExpiration(accessToken);

        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        if (redisTemplate.opsForValue().get("RT:" + userId) != null){
            redisTemplate.delete("RT:" + userId);
        }

        //회원정보 삭제
        editUserService.deleteUser(Long.parseLong(userId));

        return ResponseEntity.ok("");
    }

    @GetMapping("auth/info")
    public ResponseEntity<UserInfoDto> getUserInfo(HttpServletRequest request){
        String atk = jwtProvider.resolveToken(request);
        Long userId = Long.parseLong(jwtProvider.getSubjects(atk));
        Optional<User> user = userJpaRepo.findById(userId);

        return ResponseEntity.ok(UserInfoDto.of(user.get().getEmail(), user.get().getNickname()));
    }
}
