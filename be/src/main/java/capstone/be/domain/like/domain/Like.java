package capstone.be.domain.like.domain;

import capstone.be.domain.diary.domain.Diary;
import capstone.be.domain.user.domain.User;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "heart")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    public Like(User user, Diary diary) {
        this.user = user;
        this.diary = diary;
    }
}