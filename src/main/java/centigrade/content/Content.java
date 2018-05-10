package centigrade.content;

import javax.persistence.*;

@Entity
@Table(name="content")
public class Content {
    @Id
    private long id;
    private String imbdId;

    @Column(name="Id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="imbdId")
    public String getImbdId() {
        return imbdId;
    }

    public void setImbdId(String imbdId) {
        this.imbdId = imbdId;
    }
}
