package capstone.be.global.dto.signup;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialSignupRequestDto {
    private String accessToken;
    private String email;
    private String nickname;
}
