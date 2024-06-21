package capstone.be.domain.user.repository;

import capstone.be.domain.user.domain.User;
import capstone.be.domain.user.domain.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);

}
