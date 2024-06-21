package capstone.be.domain.user.service;

import capstone.be.domain.user.domain.User;
import capstone.be.domain.user.dto.EmailEditDto;
import capstone.be.domain.user.dto.NicknameEditDto;
import capstone.be.domain.user.repository.UserRepository;
import capstone.be.global.advice.exception.CUserNotFound2Exception;
import capstone.be.global.advice.exception.security.*;
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
        User user = userRepository.findById(userId).orElseThrow(CUserNotFound2Exception::new);

        Optional<User> otherUser = userRepository.findByNickname(nickname);

        if (otherUser.isEmpty()|| nickname.equals(user.getNickname())){//중복되지 않는 닉네임
            user.setNickname(nickname);//닉네임 수정
        }else{
            throw new CNicknameSignupFailedException();
        }

        //닉네임 형식 오류(20자 초과 시 예외 처리)
        if (nickname == null || nickname.length() > 20) {
            throw new CNicknameSignupFailed2Exception();
        }

        userRepository.save(user);//트랜잭션 있어서 없어도 저장되긴 함

        return new NicknameEditDto(user.getNickname());
    }

    //이메일 변경
    @Transactional
    public EmailEditDto editEmail(Long userId, String email) {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFound2Exception::new);

        Optional<User> otherUser = userRepository.findByEmail(email);

        if (otherUser.isEmpty()|| email.equals(user.getEmail())){//중복되지 않는 이메일
            user.setEmail(email);//닉네임 수정
        }else{
            throw new CEmailSignupFailedException();
        }

        //이메일 형식 오류(이메일에 @가 없는 경우 예외 처리)
       if (email == null || !email.contains("@")) {
            throw new CWrongEmailFailedException();
        }

        userRepository.save(user);//트랜잭션 있어서 없어도 저장되긴 함

        return new EmailEditDto(user.getEmail());
    }

    //회원 탈퇴
    @Transactional
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(CUserNotFound2Exception::new);
        userRepository.delete(user); //유저 정보 삭제
    }
}
