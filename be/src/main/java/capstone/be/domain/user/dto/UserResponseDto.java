package capstone.be.domain.user.dto;


import capstone.be.domain.user.domain.User;
import capstone.be.domain.user.domain.UserPrincipal;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserResponseDto {
    private final Long userId;
    private final String email;
    private final String nickName;
    private List<String> roles;
    private Collection<? extends GrantedAuthority> authorities;
    private final LocalDateTime modifiedDate;

    public UserResponseDto(User user) {
        Set<UserPrincipal.RoleType> roleTypes = Set.of(UserPrincipal.RoleType.USER);
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.nickName = user.getNickname();
        this.roles = Collections.singletonList("ROLE_USER");
        this.authorities = roleTypes.stream()
                .map(UserPrincipal.RoleType::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
        this.modifiedDate = user.getModifiedAt();
    }
}
