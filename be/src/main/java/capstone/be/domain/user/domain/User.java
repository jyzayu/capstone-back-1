package capstone.be.domain.user.domain;

import capstone.be.global.entity.AuditingFields;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt")
//        @Index(columnList = "createdBy")
})
@Entity
public class User extends AuditingFields {
    @Id
    private Long userId;

    @Setter
    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Setter
    @Column(length = 20,unique = true, nullable = false)
    private String nickname;



    protected User() {
    }

    private User(Long userId, String email, String nickname) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
//        this.createdBy = createdBy;
//        this.modifiedBy = createdBy;
    }

    public static User of(Long userId, String email, String nickname) {
        return new User(userId, email, nickname);
    }



}