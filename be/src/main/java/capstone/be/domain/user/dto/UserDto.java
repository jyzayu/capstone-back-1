package capstone.be.domain.user.dto;

import capstone.be.domain.user.domain.User;

public record UserDto(
        Long userId,
        String email,
        String password,
        String nickname
) {
    public static UserDto of(Long userId,String email,String password,String nickname){
        return new UserDto(userId,email,password,nickname);
    }

    public static  UserDto from(User user){
        return new UserDto(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname()
        );
    }

    public User toEntity(){
        return User.of(
                userId,
                email,
                password,
                nickname
        );
    }
}
