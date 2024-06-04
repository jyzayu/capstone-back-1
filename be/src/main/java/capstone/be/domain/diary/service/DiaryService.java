package capstone.be.domain.diary.service;

import capstone.be.domain.diary.domain.BProperties;
import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.dto.DiaryCreatedDto;
import capstone.be.domain.diary.dto.DiaryDto;
import capstone.be.domain.diary.dto.Posts;
import capstone.be.domain.diary.dto.response.CalendarResponse;
import capstone.be.domain.diary.dto.response.DiaryMainTotalResponse;
import capstone.be.domain.diary.dto.response.DiaryMoodTotalResponse;
import capstone.be.domain.hashtag.dto.HashtagDto;
import capstone.be.domain.diary.dto.response.DiaryCreateResponse;
import capstone.be.domain.diary.dto.response.DiaryMoodTotalResponse;
import capstone.be.domain.diary.repository.DiaryRepository;
import capstone.be.domain.hashtag.domain.Hashtag;
import capstone.be.domain.hashtag.dto.HashtagDto;
import capstone.be.domain.hashtag.service.HashtagService;
import capstone.be.global.advice.exception.diary.CDiaryNotFoundException;
import capstone.be.global.advice.exception.diary.CDiaryPastEditException;
import capstone.be.global.advice.exception.diary.CMoreNewDiaryException;
import capstone.be.s3.AmazonS3Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final HashtagService hashtagService;
    private final EntityManager entityManager;
    private final AmazonS3Service amazonS3Service;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<Long, Object> postTemplate;
    private static final String POPULAR_POSTS_KEY = "popularPosts";
    private static final long CACHE_EXPIRATION = 10; // 캐시 만료 시간 (분 단위)

    public DiaryCreateResponse save(DiaryDto diaryDto) throws IOException {
        Diary diary = diaryDto.toEntity();

        Set<Hashtag> hashtags = renewHashtagsFromContent(diaryDto);
        diary.addHashtags(hashtags);

        LocalDate today = LocalDate.now();
//        List<Diary> diaryList = diaryRepository.findByUserIdAndCreatedAt(diaryDto.getUserId(), today);
//        Boolean existingDiary = !diaryList.isEmpty();
//        if(existingDiary){
//            //System.out.println(" 이미 오늘 일기를 작성했습니다. ");
//            throw new CMoreNewDiaryException();
//          }

        String thumbnailUrl;
        Optional<BProperties> bProperties = diaryDto.getBlocks().stream().filter(x -> x.getType().equals("img")).findFirst();
        if(bProperties.isPresent()) {//썸네일 이미지가 있으면
            String OriginUrl = bProperties.get().getData().getLink();
            thumbnailUrl = amazonS3Service.uploadThumbnail(OriginUrl, "thumbnails", 300, 400);
        }else{//없을 때 임시 썸네일 링크
            thumbnailUrl = "https://ljgs3test.s3.ap-northeast-2.amazonaws.com/default/a2532aff-5ce6-4165-b433-479d52cbd16912402fec-be9c-4306-a61f-677c5dd291be_thumbnail.jpg";
        }
        diary.setThumbnail(thumbnailUrl);

        //title이 비어있을 경우 첫번째 블록 내용을 제목으로 대체
        String title = diaryDto.getTitle();
        //System.out.println("title = " + title);
        BProperties firstBlock = diaryDto.getBlocks().get(0);

        //System.out.println("title = " + title);
        if (title == null || title.isBlank()) {
            if(firstBlock.getType().equals("img")){
                title = "(이미지)";
            }else{
                title = firstBlock.getData().getText().replaceAll("<.*?>", "");
            }
            diary.setTitle(title);
        }
      

        return new DiaryCreateResponse(diaryRepository.save(diary).getId());
    }



    @Transactional(readOnly = true)
    public DiaryCreatedDto getDiary(Long diaryId, Long userId){
        Optional<Diary> diary = (Optional<Diary>) postTemplate.opsForValue().get(diaryId);

        if (diary.isEmpty()) {
            diary = diaryRepository.findById(diaryId);
        }
        diary.get().setViewCount(diary.get().getViewCount() + 1);
        return diary
                .map(DiaryCreatedDto::from)
                .orElseThrow(() -> new CDiaryNotFoundException());
//
//        return diaryRepository.findByIdAndUserId(diaryId, userId)
//                .map(DiaryCreatedDto::from)
//                .orElseThrow(() -> new CDiaryNotFoundException());
    }

    @Transactional
    public Page<Diary> getAllDiary(Long userid,int page, int size){
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page,size,sort);
            return diaryRepository.findByUserId(userid,pageable);
    }

    @Transactional
    public void deleteAllDiariesByUserId(Long userId) {
        Page<Diary> diaries = getAllDiary(userId, 0, 10);

        List<Diary> userdiaries = diaries.getContent();

        for (Diary diary : userdiaries) {
            diaryRepository.delete(diary);
        }
      }

    @Transactional
    public Page<Diary> getSearchDiaryTitle(String content, int page,int size,Long userid){
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Diary> postList;
        Set<Diary> postSet = new HashSet<>();

        if(content.startsWith(".")) {
            postList = diaryRepository.findHashSearchList(content.substring(1), userid, pageable);
        }
        else{
            sort = Sort.by("created_at").descending();
            pageable = PageRequest.of(page,size,sort);


            postSet.addAll(diaryRepository.findSearchList(content, pageable).getContent());
            postList =new PageImpl<>(new ArrayList<>(postSet), pageable, postSet.size());
        }


        if (postList.isEmpty() && content.equals(""))
               postList = diaryRepository.findAllList(userid,pageable);
        return postList;
    }

    public void updateDiary(Long diaryId, DiaryDto dto){
      try {
            Diary diary = diaryRepository.getReferenceById(diaryId);

            //DIARY_009
            LocalDate currentDate = LocalDate.now();
            LocalDate creationDate = diary.getCreatedAt().toLocalDate();
            if(!currentDate.equals(creationDate)){
                throw new CDiaryPastEditException();
            }

            String title = dto.getTitle();
            //System.out.println("title = " + title);
            BProperties firstBlock = dto.getBlocks().get(0);

            if (title == null || title.isBlank()) {
                if(firstBlock.getType().equals("img")){
                    title = "(이미지)";
                }else{
                    title = firstBlock.getData().getText().replaceAll("<.*?>", "");
                }
                diary.setTitle(title);

            }else{ diary.setTitle(dto.getTitle().replaceAll("<.*?>", "")); }
            if (dto.getWeather() != null) { diary.setWeather(dto.getWeather()); }
            if (dto.getFont() != null) { diary.setFont(dto.getFont()); }
            if (dto.getMood() != null) { diary.setMood(dto.getMood()); }
            if (dto.getBlocks() != null) { diary.setBlocks(dto.getBlocks()); }
            if (dto.getThumbnail() != null) { diary.setThumbnail(dto.getThumbnail()); }
        
            Set<Long> hashtagIds = diary.getHashtags().stream()
                    .map(Hashtag::getId)
                    .collect(Collectors.toUnmodifiableSet());
            diary.clearHashtags();
            diaryRepository.flush();
            hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);

            Set<Hashtag> hashtags = renewHashtagsFromContent(dto);
            diary.addHashtags(hashtags);

        }catch (EntityNotFoundException e){
            // Diary_008
            throw new CDiaryNotFoundException();
        }
    }


    public void deleteDiary(Long diaryId,Long userId) {
        Diary diary = diaryRepository.getReferenceById(diaryId);
        if(!diary.getUserId().equals(userId))
            throw new CDiaryNotFoundException();

        Set<Long> hashtagIds = diary.getHashtags().stream()
                .map(Hashtag::getId)
                .collect(Collectors.toUnmodifiableSet());

        diaryRepository.deleteById(diaryId);
        diaryRepository.flush();
      
        hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);
    }

    //다이어리 기분별 서치
    public Page<Diary> getSortedDiariesByMood(String mood, int page, int size, Long userId) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Diary> postList = diaryRepository.findByUserIdAndMood(userId, mood, pageable);
        return postList;
    }



    //마이페이지 들어갈 때 전체 기분별 다이어리 개수 보내주기
    public DiaryMoodTotalResponse getMoodTotal(Long userId) {
        String sql = "SELECT " +
                "SUM(CASE WHEN mood = 'best' THEN 1 ELSE 0 END) AS bestCount, " +
                "SUM(CASE WHEN mood = 'good' THEN 1 ELSE 0 END) AS goodCount, " +
                "SUM(CASE WHEN mood = 'normal' THEN 1 ELSE 0 END) AS normalCount, " +
                "SUM(CASE WHEN mood = 'bad' THEN 1 ELSE 0 END) AS badCount, " +
                "SUM(CASE WHEN mood = 'worst' THEN 1 ELSE 0 END) AS worstCount " +
                "FROM diary WHERE user_id = ?";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, userId);

        Object[] result = (Object[]) query.getSingleResult();
       Long best = ((BigDecimal) (result[0] != null ? result[0] : BigDecimal.ZERO)).longValue();
        Long good = ((BigDecimal) (result[1] != null ? result[1] : BigDecimal.ZERO)).longValue();
        Long normal = ((BigDecimal) (result[2] != null ? result[2] : BigDecimal.ZERO)).longValue();
        Long bad = ((BigDecimal) (result[3] != null ? result[3] : BigDecimal.ZERO)).longValue();
        Long worst = ((BigDecimal) (result[4] != null ? result[4] : BigDecimal.ZERO)).longValue();

        return new DiaryMoodTotalResponse(best, good, normal, bad, worst);
    }

    //다이어리 캘린더 가져오기
    public List<CalendarResponse> getDiaryByMonth(int year, int month, Long userId){
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);
        List<CalendarResponse> calendarList = diaryRepository.findByUserIdAndCreatedAtBetween(userId,startDate,endDate);

        return calendarList;
    }

    private Set<Hashtag> renewHashtagsFromContent(DiaryDto diaryDto) {
        Set<String> hashtagNamesInContent = diaryDto.getHashtag().stream().map(HashtagDto::getHashtagName).collect(Collectors.toUnmodifiableSet());
        Set<Hashtag> hashtags = hashtagService.findHashtagsByNames(diaryDto.getHashtag().stream().map(HashtagDto::getHashtagName).collect(Collectors.toUnmodifiableSet()));
        Set<String> existingHashtagNames = hashtags.stream().map(Hashtag::getHashtagName).collect(Collectors.toUnmodifiableSet());

        hashtagNamesInContent.forEach(newHashtagName -> {
          if (!existingHashtagNames.contains(newHashtagName)) {
                hashtags.add(Hashtag.of(newHashtagName));
            }
        });

        return hashtags;
    }

//    @Cacheable(value = "popularPosts", key = "10", cacheManager = "redisCacheManager")
    public Posts getPopularDiaries(){
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        List<Diary> popularPosts = (List<Diary>) redisTemplate.opsForValue().get(POPULAR_POSTS_KEY);


        if (CollectionUtils.isEmpty(popularPosts)) {
            popularPosts = diaryRepository.findPopularDiaries();
            try {
                for (Diary popularPost : popularPosts) {
                    String post = mapper.writeValueAsString(popularPost);
                    postTemplate.opsForValue().set(popularPost.getId(), post, CACHE_EXPIRATION, TimeUnit.MINUTES);
                }
//                redisTemplate.opsForValue().set(POPULAR_POSTS_KEY, popularPosts, CACHE_EXPIRATION, TimeUnit.MINUTES);
            }
            catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return new Posts(popularPosts);
    }
}
