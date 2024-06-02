package capstone.be.domain.user.repository;

import capstone.be.domain.user.domain.User;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user) {
        String key = getKey(user.getUserId());
        log.info("Set User to Redis {} : {}", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<User> getUser(Long userId) {
        User user = userRedisTemplate.opsForValue().get(getKey(userId));
        log.info("Get User from Redis {} : {}", getKey(userId), user);
        return Optional.ofNullable(user);
    }

    public String getKey(Long userId) {
        return "USER:" + userId;
    }

}
