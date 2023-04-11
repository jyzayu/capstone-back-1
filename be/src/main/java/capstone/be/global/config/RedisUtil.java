package capstone.be.global.config;

import capstone.be.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisUtil<K, V> {
    private final RedisTemplate<K, V> redisTemplate;
    private final RedisTemplate<K, V> redisBlackListTemplate;
    private final JwtProvider jwtProvider;

    public void setValue(K key, V value) {
        ValueOperations<K, V> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setValue(K key, List<V> value) {
        redisTemplate.opsForList().rightPush(key, value.get(0));
        redisTemplate.opsForList().rightPush(key, value.get(1));
//        redisTemplate.expire(key, Long.parseLong(jwtConfig.getRefreshExpiry()), TimeUnit.MILLISECONDS);
    }

    public void setValue(K key, K hashKey, V value) {
        HashOperations<K, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, hashKey, value);
    }

    public void setValueBlackList(K key, V value, Long milliSeconds) {
        redisBlackListTemplate.opsForValue().set(key, value, milliSeconds, TimeUnit.MILLISECONDS);
    }

    public V getValue(K key) {
        ValueOperations<K, V> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public List<V> getValues(K key) {
        ListOperations<K, V> values = redisTemplate.opsForList();
        return values.range(key, 0, 1);
    }

    @SuppressWarnings(value = "unchecked")
    public V getHashValue(K key, K hashKey) {
        HashOperations<K, Object, Object> values = redisTemplate.opsForHash();
        return (V) values.get(key, hashKey);
    }

    @SuppressWarnings(value = "unchecked")
    public Map<K, V> getHashKeys(K key) {
        HashOperations<K, Object, Object> values = redisTemplate.opsForHash();
        return (Map<K, V>) values.entries(key);
    }

    public void deleteValue(K key) {
        redisTemplate.delete(key);
    }

    public void deleteHashValue(K key, K hashKey) {
        HashOperations<K, Object, Object> values = redisTemplate.opsForHash();
        values.delete(key, hashKey);
    }

    public boolean hasKeyBlackList(K key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
    }
}