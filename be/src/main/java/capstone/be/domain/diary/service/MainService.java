package capstone.be.domain.diary.service;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryRandomDto;
import capstone.be.domain.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MainService {
    private final DiaryRepository diaryRepository;

    public DiaryRandomDto getRandomDiary(){
//        List<DiaryRandomDto> dto = diaryRepository.findRandom();
        Long qty = diaryRepository.count();
        int idx = (int)(Math.random() * qty);
        Page<Diary> questionPage = diaryRepository.findAll(PageRequest.of(idx, 1));
        Diary diary = null;
        if (questionPage.hasContent()) {
            diary = questionPage.getContent().get(0);
        }
        DiaryRandomDto dto1 = DiaryRandomDto.from(diary);
        return dto1;
    }

}
