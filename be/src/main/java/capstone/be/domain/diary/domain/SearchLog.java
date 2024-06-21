package capstone.be.domain.diary.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchLog {
    private String name;
    private String createdAt;
}
