package capstone.be.domain.user.repository;

import capstone.be.domain.user.domain.User;
import java.time.Duration;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final RedisTemplate<String, String> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user) throws JsonProcessingException {
        String key = getKey(user.getUserId());
        log.info("Set User to Redis {} : {}", key, user);
        String userStr = mapper.writeValueAsString(user);

        userRedisTemplate.opsForValue().set(key, userStr, USER_CACHE_TTL);
    }

    public Optional<User> getUser(Long userId) throws JsonProcessingException {
        String user = String.valueOf(userRedisTemplate.opsForValue().get(getKey(userId)));
        log.info("Get User from Redis {} : {}", getKey(userId), user);
        return Optional.ofNullable(mapper.readValue(user, User.class));
    }

    public String getKey(Long userId) {
        return "USER:" + userId;
    }

}
