package capstone.be.domain.diary.domain;


import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
// Todo: Equals HashCode override하여 select시 update쿼리를 없애도록 하면 N+1 문제인가? hashtag 연관관계가 없는데 모두 조회하는
// 문제가 발생하여 더 오래걸려버림
//@EqualsAndHashCode
public class BProperties {


    public Long id =0L ;
    private String type;
    private BPropertiesData data;

    public Long getId() {
        return id;
    }

    public void setId(Long id2) {
            this.id = id2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BPropertiesData getData() {
        return data;
    }

    public void setData(BPropertiesData data) {
        this.data = data;
    }


}
