

package capstone.be.domain.user.controller;


import capstone.be.domain.user.dto.RetKakaoOAuth;
import capstone.be.domain.user.service.KakaoService;
import capstone.be.domain.user.service.SignService;
import capstone.be.global.dto.response.ResponseService;
import capstone.be.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final SignService signService;

    private final KakaoService kakaoService;

    private final ResponseService responseService;

    private final JwtProvider jwtProvider;

// code를 프론트 통신할 떄 보내주면 해당 코드는 사용하지 않음, 테스트를 위해 사용
@GetMapping("/login/oauth2/code/kakao")
public ResponseEntity<String> getAuthCode(@RequestParam String code) throws Exception {
//     code로 token 요청     -> login api에서 code로 token을 요청하기위해  코드 응답
//    RetKakaoOAuth tokenInfo = kakaoService.getKakaoTokenInfo(code);
//    String access_Token = tokenInfo.getAccess_token();
    return ResponseEntity.ok(code);
}
}
