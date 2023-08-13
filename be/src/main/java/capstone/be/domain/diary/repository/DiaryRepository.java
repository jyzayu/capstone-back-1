package capstone.be.domain.diary.repository;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.response.CalendarResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Page<Diary> findByUserIdAndMood(Long userId, String mood, Pageable pageable);

    Page<Diary> findByUserId(Long userid,Pageable pageable);
    Optional<Diary> findByIdAndUserId(Long id, Long userId);
    List<CalendarResponse> findByUserIdAndCreatedAtBetween(Long userId,LocalDateTime startDate, LocalDateTime endDate);

    Long countByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    Long countByUserId(Long userId);

//    @Query(nativeQuery = true, value = "SELECT d.title, d.weather, d.mood FROM diary d ORDER BY RAND() LIMIT 1")
//    List<DiaryRandomDto> findRandom();


    @Query(value = "SELECT * from diary  where blocks like %:content% or title like %:content% and user_id = :userid ",nativeQuery = true)
    Page<Diary> findSearchList(String content,Long userid,Pageable pageable);

}
