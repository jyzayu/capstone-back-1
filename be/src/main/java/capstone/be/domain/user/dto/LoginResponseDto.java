package capstone.be.domain.user.dto;

public record LoginResponseDto(
        String accessToken,
        Boolean newUser) {

}
