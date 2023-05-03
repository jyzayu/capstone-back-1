package capstone.be.domain.user.service;

import capstone.be.domain.user.domain.User;
import capstone.be.domain.user.repository.UserRepository;
import capstone.be.global.advice.exception.*;
import capstone.be.global.dto.jwt.ReissueDto;
import capstone.be.global.dto.jwt.TokenDto;
import capstone.be.global.dto.jwt.TokenRequestDto;
import capstone.be.global.dto.signup.UserLoginRequestDto;
import capstone.be.global.dto.signup.UserSignupRequestDto;
import capstone.be.global.jwt.JwtProvider;
import capstone.be.global.jwt.RefreshToken;
import capstone.be.global.jwt.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {
    private final UserRepository userJpaRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;
    private final RefreshTokenJpaRepository tokenJpaRepo;

    @Transactional
    public TokenDto login(UserLoginRequestDto userLoginRequestDto) {

        // 회원 정보 존재하는지 확인
        User user = userJpaRepo.findByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(CEmailLoginFailedException::new);

        // 회원 패스워드 일치 여부 확인
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword()))
            throw new CEmailLoginFailedException();

        // AccessToken, RefreshToken 발급 Todo: 권한 구분 구현 아직 x role은 User로 고정시켜 구현
        TokenDto tokenDto = jwtProvider.createTokenDto(user.getUserId(), Collections.singletonList("ROLE_USER"));
        return tokenDto;
    }

    @Transactional
    public Long signup(UserSignupRequestDto userSignupDto) {
        if (userJpaRepo.findByEmail(userSignupDto.getEmail()).isPresent())
            throw new CNicknameSignupFailedException();
        return userJpaRepo.save(userSignupDto.toEntity(passwordEncoder)).getUserId();
    }


    @Transactional
    public Long socialSignup(UserSignupRequestDto userSignupRequestDto) {
        if (userJpaRepo
                .findByEmail(userSignupRequestDto.getEmail())
                .isPresent()
        ) throw new CUserExistException();
        return userJpaRepo.save(userSignupRequestDto.toEntity()).getUserId();
    }


    @Transactional
    public ReissueDto reissue(HttpServletRequest request) {

        // request 에서 token 을 취한다.
        String rtk = jwtProvider.resolveToken(request);

        // 만료된 refresh token 에러
        if (!jwtProvider.validationToken(rtk)) {
            throw new CRefreshTokenException();
        }

        //3. 저장된 refresh token 찾기
        Long userId = Long.parseLong(jwtProvider.getSubjects(rtk));
        String newRTK = (String) redisTemplate.opsForValue().get("RT:" + userId);

        if (ObjectUtils.isEmpty(newRTK) || !newRTK.equals(rtk)){
            throw new CRefreshTokenException();
        }
        TokenDto tokenDto = jwtProvider.createTokenDto(userId, Collections.singletonList("ROLE_USER"));
        //Todo: 4. redis에 있는 RTK update 정상동작하는지?
        redisTemplate.opsForValue().set("RT:" + userId, rtk, jwtProvider.getExpiration(newRTK), TimeUnit.MILLISECONDS);

        return new ReissueDto(tokenDto.getAccessToken());
    }
}
