package capstone.be.domain.diary.repository;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryDto;
import capstone.be.domain.diary.dto.response.CalendarResponse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Page<Diary> findByMood(String mood, Pageable pageable);
    List<CalendarResponse> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<Diary> findByTitleContains(String content, Pageable pageable);


    @Query(value = "SELECT * from diary  where blocks like %:content% or title like %:content%",nativeQuery = true)
    Page<Diary> findSearchList(String content,Pageable pageable);

}
