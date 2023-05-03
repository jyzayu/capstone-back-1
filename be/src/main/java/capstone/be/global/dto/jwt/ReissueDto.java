package capstone.be.global.dto.jwt;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReissueDto {
    private String accessToken;

}
