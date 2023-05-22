package capstone.be.domain.diary.repository;

import capstone.be.domain.diary.domain.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Page<Diary> findByMood(String mood, Pageable pageable);
}
