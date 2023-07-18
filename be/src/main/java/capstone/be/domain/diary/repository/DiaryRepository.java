package capstone.be.domain.diary.repository;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.response.CalendarResponse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Page<Diary> findByMood(String mood, Pageable pageable);
    List<CalendarResponse> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

//    @Query(nativeQuery = true, value = "SELECT d.title, d.weather, d.mood FROM diary d ORDER BY RAND() LIMIT 1")
//    List<DiaryRandomDto> findRandom();




}
