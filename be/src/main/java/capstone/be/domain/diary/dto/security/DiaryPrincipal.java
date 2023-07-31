package capstone.be.domain.diary.dto.security;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.user.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public record DiaryPrincipal(
        Long userId,
        String email,
        Collection<? extends GrantedAuthority> authorities,
        String password,
        String nickname,
        Map<String, Object> oAuth2Attributes
) implements UserDetails, OAuth2User{
    public static DiaryPrincipal of(Long userId,String email,String password,String nickname){
        return DiaryPrincipal.of(userId, email, password, nickname);
    }

    public static  DiaryPrincipal from(UserDto dto){
        return DiaryPrincipal.of(
                dto.userId(),
                dto.email(),
                dto.password(),
                dto.nickname()
        );
    }

    public UserDto toDto(){
        return UserDto.of(
                userId,
                email,
                password,
                nickname
        );
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
