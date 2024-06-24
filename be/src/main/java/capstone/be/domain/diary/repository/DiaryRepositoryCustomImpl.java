package capstone.be.domain.diary.repository;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.diary.domain.QDiary;
import capstone.be.domain.diary.dto.DiarySearchDto;
import capstone.be.domain.diary.dto.TextSearchDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class DiaryRepositoryCustomImpl {

    @Autowired
    private EntityManager entityManager;

    public Page<DiarySearchDto> findDiariesByContent(String content, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDiary diary = QDiary.diary;

        // Define the JSON_UNQUOTE function
        StringTemplate jsonUnquote = Expressions.stringTemplate("JSON_UNQUOTE(JSON_EXTRACT({0}, '$[*].data.text'))", diary.blocks);

        // Perform the query
        QueryResults<DiarySearchDto> queryResults = queryFactory
                .select(Projections.constructor(DiarySearchDto.class,
                        diary.id,
                        diary.title,
                        diary.weather,
                        diary.mood,
                        diary.createdAt,
                        diary.combinedBlockText

                ))
                .from(diary)
                .where(jsonUnquote.like("%" + content + "%")
                        .or(diary.title.like("%" + content + "%")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<DiarySearchDto> results = queryResults.getResults();
        long total = queryResults.getTotal();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<DiarySearchDto> findAll(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDiary diary = QDiary.diary;

        // Define the JSON_UNQUOTE function
        StringTemplate jsonUnquote = Expressions.stringTemplate("JSON_UNQUOTE(JSON_EXTRACT({0}, '$[*].data.text'))", diary.blocks);

        // Perform the query
        QueryResults<DiarySearchDto> queryResults = queryFactory
                .select(Projections.constructor(DiarySearchDto.class,
                        diary.id,
                        diary.title,
                        diary.weather,
                        diary.mood,
                        diary.createdAt,
                        diary.combinedBlockText

                ))
                .from(diary)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<DiarySearchDto> results = queryResults.getResults();
        long total = queryResults.getTotal();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<DiarySearchDto> findByContent(String content, Pageable pageable) {

            JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
            QDiary diary = QDiary.diary;


            // Perform the query
            List<DiarySearchDto> queryResults = queryFactory
                    .select(Projections.constructor(DiarySearchDto.class,
                            diary.id,
                            diary.title,
                            diary.weather,
                            diary.mood,
                            diary.createdAt,
                            diary.combinedBlockText
                    ))
                    .from(diary)
                    .where(diary.combinedBlockText.like("%" + content + "%"))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();


            return new PageImpl<>(queryResults, pageable, pageable.getPageSize());
        }


    public Page<DiarySearchDto> findByText(String content, Pageable pageable) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDiary diary = QDiary.diary;

        // Define the MATCH ... AGAINST function
        StringTemplate matchAgainst = Expressions.stringTemplate(
                "MATCH({0}) AGAINST ({1} IN BOOLEAN MODE)",
                diary.combinedBlockText,
                content);

        // Perform the query
        List<DiarySearchDto> queryResults = queryFactory
                .select(Projections.constructor(DiarySearchDto.class,
                        diary.id,
                        diary.title,
                        diary.weather,
                        diary.mood,
                        diary.createdAt,
                        diary.combinedBlockText
                ))
                .from(diary)
                .where(Expressions.booleanTemplate("{0}", matchAgainst))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        return new PageImpl<>(queryResults, pageable, pageable.getPageSize());
    }
    }