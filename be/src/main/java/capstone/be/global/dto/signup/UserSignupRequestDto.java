package capstone.be.global.dto.signup;


import capstone.be.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequestDto {

    private Long userId;
    private String email;
    private String password;
    private String nickname;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.of(userId, email, passwordEncoder.encode(password), nickname);
    }

    public User toEntity() {
        return User.of(userId, email, password, nickname);
    }
}
