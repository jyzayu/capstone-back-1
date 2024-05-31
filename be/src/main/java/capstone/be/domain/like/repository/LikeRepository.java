package capstone.be.domain.like.repository;


import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.like.domain.Like;
import capstone.be.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    public Optional<Like> findByUserAndDiary(User user, Diary diary);
    //삭제
    void deleteByUserAndDiary(User user, Diary diary);
}
