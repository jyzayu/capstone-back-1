package capstone.be.domain.diary.domain;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


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
