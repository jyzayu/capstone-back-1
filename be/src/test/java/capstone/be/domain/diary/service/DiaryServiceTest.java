package capstone.be.domain.diary.service;

import capstone.be.domain.diary.dto.DiaryDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest()
class DiaryServiceTest {

    @MockBean private  DiaryService diaryService;
    @Test
    void save() {
        DiaryDto diaryDto = new DiaryDto("title1", "sunny", "tag1", "happy");
    }

    @Test
    void getDiary() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}