package capstone.be.domain.user.domain;

import capstone.be.global.entity.AuditingFields;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal extends AuditingFields implements UserDetails {

    private Long userId;

    private String email;
    private String password;
    private String nickname;
    private List<String> roles = new ArrayList<>();


    private UserPrincipal(Long userId, String email, String userPassword, String nickname) {
        this.userId = userId;
        this.email = email;
        this.password = userPassword;
        this.nickname = nickname;
//        this.createdBy = createdBy;
//        this.modifiedBy = createdBy;
        this.roles = Collections.singletonList("ROLE_USER");
    }

    public static UserPrincipal of(Long userId, String email, String password, String nickname) {
        return new UserPrincipal(userId, email, password, nickname);
    }

    public static UserPrincipal from(User dto) {
        return UserPrincipal.of(
                dto.getUserId(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getNickname()
        );
    }

    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return String.valueOf(this.userId);
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum RoleType {
        USER("ROLE_USER");

        @Getter private final String name;

        RoleType(String name) {
            this.name = name;
        }
    }

}