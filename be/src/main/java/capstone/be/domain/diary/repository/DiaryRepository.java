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

    @Query(nativeQuery = true, value = "SELECT * FROM diary d WHERE user_id = :userId ORDER BY RAND() LIMIT 1")
    List<Diary> findRandom(@Param("userId") Long userId);

    @Query(value = "SELECT * from diary  where user_id = :userid ",nativeQuery = true)
    Page<Diary> findAllList(Long userid,Pageable pageable);

    @Query(value = "SELECT * from diary  where ((blocks like %:content% or title like %:content%) and user_id = :userid) ",nativeQuery = true)
    Page<Diary> findSearchList(String content,Long userid,Pageable pageable);

    @Query(value ="select * from (SELECT * from diary d left join diary_hashtag on d.id = diary_id )as a left join hashtag h  on hashtag_id = h.id where (hashtag_name=:content and user_id=:userid)",nativeQuery = true)
    Page<Diary> findHashSearchList(String content,Long userid,Pageable pageable);

    @Query(value = "SELECT * FROM diary WHERE user_id = :userid AND date(created_at) = :date", nativeQuery = true)
    List<Diary> findByUserIdAndCreatedAt(Long userid, LocalDate date);

}
