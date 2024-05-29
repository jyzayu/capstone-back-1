package capstone.be.domain.diary.service;

import capstone.be.domain.diary.domain.SearchLog;
import capstone.be.domain.diary.dto.request.SearchLogDeleteRequest;
import capstone.be.domain.diary.dto.request.SearchLogSaveRequest;
import capstone.be.global.advice.exception.diary.CSearchLogNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class SearchLogService {

    private final RedisTemplate<String, SearchLog> redisTemplate;

    public void saveRecentSearchLog(Long memberId, SearchLogSaveRequest request) {
        String now = LocalDateTime.now().toString();
        String key = "searchLog" + memberId;
        SearchLog value = SearchLog.builder().name(request.getName()).createdAt(now).build();

        long size = redisTemplate.opsForList().size(key);
        if (size == 10) {
            redisTemplate.opsForList().rightPop(key);
        }
        redisTemplate.opsForList().leftPush(key, value);
    }

    public List<SearchLog> findRecentSearchLogs(Long memberId) {
        String key = "searchLog" + memberId;
        List<SearchLog> searchLogs= redisTemplate.opsForList().range(key, 0,10);
        return searchLogs;
    }

    public void deleteRecentSearchLog(Long memberId, SearchLogDeleteRequest request) {
        String key = "SearchLog" + memberId;
        SearchLog value = SearchLog.builder()
                .name(request.getName())
                .createdAt(request.getCreatedAt())
                .build();

        long count = redisTemplate.opsForList().remove(key, 1, value);

        if (count == 0) {
            throw new CSearchLogNotFoundException();
        }
    }
}
