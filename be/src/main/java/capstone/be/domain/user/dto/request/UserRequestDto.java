package capstone.be.domain.user.dto.request;


import capstone.be.domain.user.domain.User;
import capstone.be.domain.user.domain.UserPrincipal;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private Long userId;
    private String email;
    private String name;
    private String nickname;

    public User toEntity() {
        return User.of(userId, email, nickname);
    }
}
