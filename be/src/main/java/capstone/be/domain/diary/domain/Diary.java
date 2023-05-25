package capstone.be.domain.diary.domain;

import capstone.be.global.entity.AuditingFields;
import lombok.Setter;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.List;



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

    @Setter
    private String font;

    @Setter
    private String hashtag;
    @Setter
    private String mood;


    @Type(type="json")
    @Column(columnDefinition = "LONGTEXT")
    @Setter
    private List<BProperties> blocks;


    //Todo: Block 구현 Entity로 구현하면 될듯 


    private Diary(String title, String weather, String hashtag, String mood,String font,List<BProperties> blocks) {
        this.title = title;
        this.weather = weather;
        this.hashtag = hashtag;
        this.font=font;
        this.mood = mood;
        this.blocks =blocks;
    }

    public Diary() {

    }


    public static Diary of(String title, String weather, String hashtag, String mood,String font,List<BProperties> blocks){
        return new Diary(title, weather, hashtag, mood, font , blocks);
    }
    //hashtag의 경우 필드에 string으로 저장시 태그목록 조회하면서 hashtag 값 전체를 조회하는데 diary에 들어있으면 diary를 전부 조회해야하고, 그 안에 잇는 값을 저장하는 로직을 만들어야함
    // hashtag entity를 만들어 diary와 연관관계 설정을 해준다.

    //getter setter는  @getter 어노테이션 사용이  간결하고,  setter의 경우 entity가 자유로운 수정이 되면 위험하기 떄문에
    // setter는 필요한 필드에만 붙여서 사용합니다.


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWeather() {
        return weather;
    }

    public String getHashtag() {
        return hashtag;
    }

    public String getMood() {
        return mood;
    }

    public String getFont() {
        return font;
    }
    public List<BProperties> getBlocks() {
        return blocks;
    }
}
