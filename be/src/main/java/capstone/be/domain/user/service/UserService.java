package capstone.be.domain.user.service;

import capstone.be.domain.user.domain.User;
import lombok.AllArgsConstructor;
import capstone.be.domain.user.domain.UserPrincipal;
import capstone.be.domain.user.dto.UserResponseDto;
import capstone.be.domain.user.dto.request.UserRequestDto;
import capstone.be.domain.user.repository.UserRepository;
import capstone.be.global.advice.exception.security.CUserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userJpaRepo;

    @Transactional
    public Long save(UserRequestDto userDto) {
        User saved = userJpaRepo.save(userDto.toEntity());
        return saved.getUserId();
    }
    @Transactional(readOnly = true)
    public UserResponseDto findRandomUser() {
         Long randomId = (long) (Math.random() * 900);
         User user = userJpaRepo.findById(randomId)
                .orElseThrow(CUserNotFoundException::new);
         return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = userJpaRepo.findById(id)
                .orElseThrow(CUserNotFoundException::new);
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {
        User user = userJpaRepo.findByEmail(email)
                .orElseThrow(CUserNotFoundException::new);
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUser() {
        return userJpaRepo.findAll()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, UserRequestDto userRequestDto) {
        UserPrincipal userPrincipal = userJpaRepo
                .findById(id)
                .map(UserPrincipal::from)
                .orElseThrow(CUserNotFoundException::new);
        userPrincipal.updateNickName(userRequestDto.getNickname());
        return id;
    }

    @Transactional
    public void delete(Long id) {
        userJpaRepo.deleteById(id);
    }
}
