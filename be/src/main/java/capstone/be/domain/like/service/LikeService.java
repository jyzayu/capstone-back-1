package capstone.be.domain.like.service;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.repository.DiaryRepository;
import capstone.be.domain.like.domain.Like;
import capstone.be.domain.like.dto.LikeRequestDto;
import capstone.be.domain.like.repository.LikeRepository;
import capstone.be.domain.user.domain.User;
import capstone.be.domain.user.repository.UserRepository;
import capstone.be.global.advice.exception.diary.CDiaryNotFoundException;
import capstone.be.global.advice.exception.security.CUserNotFoundException;
import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository memberRepository;
    private final DiaryRepository boardRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public void insert(Long diaryId, Long userId) throws Exception {


        User user = memberRepository.findById(userId)
                .orElseThrow(() -> new CUserNotFoundException());
        Diary diary = boardRepository.findById(diaryId)
                .orElseThrow(() -> new CDiaryNotFoundException());

        // 이미 좋아요되어있으면 에러 반환 => 삭제, diary.getLikeCount() - 1;
        if (likeRepository.findByUserAndDiary(user, diary).isPresent()) {
            diary.setLikeCount(diary.getLikeCount() - 1);
            likeRepository.deleteByUserAndDiary(user, diary);
        }
        else{//없으면 count + 1, 저장
            diary.setLikeCount(diary.getLikeCount() + 1);
            likeRepository.save(new Like(user, diary));
        }

    }
}