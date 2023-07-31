package capstone.be.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String email;
    private String nickname;

    public static UserInfoDto of(String email, String nickname) {
        return new UserInfoDto(email, nickname);
    }
}
