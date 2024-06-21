package capstone.be.domain.diary.domain;

import lombok.EqualsAndHashCode;
// Todo: Equals HashCode override하여 select시 update쿼리를 없애도록 하면 N+1 문제인가? hashtag 연관관계가 없는데 모두 조회하는
// 문제가 발생하여 더 오래걸려버림
//@EqualsAndHashCode
public class BPropertiesData {

    private String text;
    private int level;
    private String align;
    private String link;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
