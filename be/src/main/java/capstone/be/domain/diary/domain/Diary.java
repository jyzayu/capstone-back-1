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



    //Todo: Block 구현 Entity로 구현하면 될듯 



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
