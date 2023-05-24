package capstone.be.domain.diary.domain;

import capstone.be.domain.hashtag.domain.Hashtag;
import capstone.be.global.entity.AuditingFields;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Entity
public class Diary extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(nullable = false)
    private String title;
    @Setter
    private String weather;

    @ToString.Exclude
    @JoinTable(
            name = "diary_hashtag",
            joinColumns = @JoinColumn(name = "diaryId"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Hashtag> hashtags = new LinkedHashSet<>();
    @Setter
    private String mood;

    public Diary() {

    }

    private Diary(String title, String weather, String mood) {
        this.title = title;
        this.weather = weather;
        this.mood = mood;
    }

    public static Diary of(String title, String weather, String mood){
        return new Diary(title, weather, mood);
    }
    //hashtag의 경우 필드에 string으로 저장시 태그목록 조회하면서 hashtag 값 전체를 조회하는데 diary에 들어있으면 diary를 전부 조회해야하고, 그 안에 잇는 값을 저장하는 로직을 만들어야함
    // hashtag entity를 만들어 diary와 연관관계 설정을 해준다.

    //getter setter는  @getter 어노테이션 사용이  간결하고,  setter의 경우 entity가 자유로운 수정이 되면 위험하기 떄문에
    // setter는 필요한 필드에만 붙여서 사용합니다.


    public void addHashtag(Hashtag hashtag) {
        this.getHashtags().add(hashtag);
    }

    public void addHashtags(Collection<Hashtag> hashtags) {
        this.getHashtags().addAll(hashtags);
    }

    public void clearHashtags() {
        this.getHashtags().clear();
    }

}
