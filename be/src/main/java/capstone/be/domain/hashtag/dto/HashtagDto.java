package capstone.be.domain.hashtag.dto;

import capstone.be.domain.hashtag.domain.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HashtagDto {
    Long id;
    String hashtagName;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;

    public static HashtagDto of(String hashtagName) {
        return new HashtagDto(null, hashtagName, null, null);
    }

    public static HashtagDto of(Long id, String hashtagName, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new HashtagDto(id, hashtagName, createdAt, modifiedAt);
    }

    public static HashtagDto from(Hashtag entity) {
        return new HashtagDto(
                entity.getId(),
                entity.getHashtagName(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    public Hashtag toEntity() {
        return Hashtag.of(hashtagName);
    }
}
