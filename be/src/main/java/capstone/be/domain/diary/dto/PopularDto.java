package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.Diary;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopularDto {
    Long id;
    String title;

    public PopularDto(Diary diary) {
            this.id = diary.getId();
            this.title = diary.getTitle();
        }
    }
