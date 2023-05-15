package capstone.be.domain.user.service;

import capstone.be.domain.user.domain.UserPrincipal;
import capstone.be.domain.user.repository.UserRepository;
import capstone.be.global.advice.exception.security.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userJpaRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        return userJpaRepo.findById(Long.parseLong(userPk))
                .map(UserPrincipal::from)
                .orElseThrow(CUserNotFoundException::new);
    }
}
