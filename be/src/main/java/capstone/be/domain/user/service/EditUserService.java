package capstone.be.domain.user.service;

import capstone.be.domain.user.domain.User;
import capstone.be.domain.user.dto.EmailEditDto;
import capstone.be.domain.user.dto.NicknameEditDto;
import capstone.be.domain.user.repository.UserRepository;
import capstone.be.global.advice.exception.security.CEmailSignupFailedException;
import capstone.be.global.advice.exception.security.CNicknameSignupFailedException;
import capstone.be.global.advice.exception.security.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EditUserService {
    private final UserRepository userRepository;

    //닉네임 변경
    @Transactional
    public NicknameEditDto editNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);

        Optional<User> otherUser = userRepository.findByNickname(nickname);

        if (otherUser.isEmpty()){//중복되지 않는 닉네임
            user.setNickname(nickname);//닉네임 수정
        }else{
            throw new CNicknameSignupFailedException();
        }

        //닉네임 형식 오류(닉네임 형식 추가되면 그때 구현하기로)

        userRepository.save(user);//트랜잭션 있어서 없어도 저장되긴 함

        return new NicknameEditDto(user.getNickname());
    }

    //이메일 변경
    @Transactional
    public EmailEditDto editEmail(Long userId, String email) {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);

        Optional<User> otherUser = userRepository.findByEmail(email);

        if (otherUser.isEmpty()){//중복되지 않는 이메일
            user.setEmail(email);//닉네임 수정
        }else{
            throw new CEmailSignupFailedException();
        }

        //이메일 형식 오류(이메일 형식 추가되면 그때 구현하기로)

        userRepository.save(user);//트랜잭션 있어서 없어도 저장되긴 함

        return new EmailEditDto(user.getEmail());
    }

    //회원 탈퇴
    @Transactional
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        userRepository.delete(user); //유저 정보 삭제
    }
}
