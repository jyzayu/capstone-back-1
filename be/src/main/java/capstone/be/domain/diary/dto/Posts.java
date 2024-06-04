package capstone.be.domain.diary.dto;

import capstone.be.domain.diary.domain.Diary;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Posts {
    private List<Long> ids = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public Posts(List<Diary> members) {
        for (Diary diary : members) {
            ids.add(diary.getId());
            titles.add(diary.getTitle());
        }
    }
}