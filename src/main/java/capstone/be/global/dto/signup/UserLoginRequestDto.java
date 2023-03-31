package capstone.be.global.dto.signup;

import capstone.be.domain.user.domain.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequestDto {
    private String email;
    private String password;

    public UserPrincipal toUser(PasswordEncoder passwordEncoder) {
        return UserPrincipal.of(email, password,  null);
    }
}

