package capstone.be.domain.diary.repository;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryRandomDto;
import capstone.be.domain.diary.dto.response.CalendarResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Query(nativeQuery = true, value = "SELECT * FROM diary WHERE user_id = :userId ORDER BY RAND() LIMIT 1")
    List<Diary> findRandom(@Param("userId") Long userId);


    @Query("SELECT d FROM Diary d WHERE d.userId = :userid")
    Page<Diary> findAllList(@Param("userid") Long userid, Pageable pageable);


    @Query(value = "SELECT * FROM diary WHERE (JSON_UNQUOTE(JSON_EXTRACT(blocks, CONCAT('$[', ?1, '].data.text'))) LIKE %?2% OR title LIKE %?2%) AND user_id = ?3 ", nativeQuery = true)
    Page<Diary> findSearchList(Integer idx, String content, Long userid, Pageable pageable);

    @Query("SELECT d FROM Diary d LEFT JOIN d.hashtags h WHERE h.hashtagName = :content AND d.userId = :userid")
    Page<Diary> findHashSearchList(@Param("content") String content, @Param("userid") Long userid, Pageable pageable);


    @Query(value = "SELECT * FROM diary WHERE user_id = :userid AND date(created_at) = :date", nativeQuery = true)
    List<Diary> findByUserIdAndCreatedAt(Long userid, LocalDate date);

}
