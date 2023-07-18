package capstone.be.domain.diary.service;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryRandomDto;
import capstone.be.domain.diary.dto.response.DiaryMainTotalResponse;
import capstone.be.domain.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MainService {
    private final DiaryRepository diaryRepository;

    public DiaryRandomDto getRandomDiary(){
//        List<DiaryRandomDto> dto = diaryRepository.findRandom();
        Long cnt = diaryRepository.count();
        int idx = (int) (Math.random() * cnt);

//        System.out.println(idx);
        Diary diary = null;

        Page<Diary> diaryPage = diaryRepository.findAll(PageRequest.of(idx, 1));
        if(diaryPage.hasContent()){
            diary = diaryPage.getContent().get(0);
        }
        else{
            return null;
        }
        DiaryRandomDto dto1 = DiaryRandomDto.from(diary);
        return dto1;
    }

    public DiaryMainTotalResponse getDiaryTotal(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        Month month = now.getMonth();

        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endDate = startDate.withDayOfMonth(startDate.toLocalDate().lengthOfMonth());

        LocalDateTime yStart = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime yEnd = startDate.withDayOfMonth(yStart.getMonth().maxLength());

        Long ycnt = diaryRepository.countByCreatedAtBetween(yStart, yEnd);
        Long mcnt = diaryRepository.countByCreatedAtBetween(startDate, endDate);

        return DiaryMainTotalResponse.of(ycnt, mcnt);
    }


}
