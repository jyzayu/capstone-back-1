package capstone.be.global.dto.signup;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialLoginRequestDto {

    // Todo: code는 프론트에서 nickname,email과 함께 보내주는 것으로 수정  email동의 했으면 nickname을 같이 보내준다.
    // Todo: 추후 accessToken대신 code, email, nickname을 받는다.
    // 근데 프론트에서는 카카오 토큰 요청하지 않으므로, 카카오톡 이메일을 받지 못한다.
    // 따라서 이메일 동의 했든 안 했든 이메일을 사용자가 입력받도록 구현? nickName과 같이
    private String accessToken;

}
