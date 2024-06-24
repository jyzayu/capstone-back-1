package capstone.be.domain.user.service;

import capstone.be.domain.user.domain.UserPrincipal;
import capstone.be.domain.user.repository.UserCacheRepository;
import capstone.be.domain.user.repository.UserRepository;
import capstone.be.global.advice.exception.security.CUserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userJpaRepo;
    private final UserCacheRepository userCacheRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        try {
            return userCacheRepository.getUser(Long.parseLong(userPk)).map(UserPrincipal::from).orElseGet(() ->
                    userJpaRepo.findById(Long.parseLong(userPk)).map(UserPrincipal::from)
                            .orElseThrow(CUserNotFoundException::new));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
