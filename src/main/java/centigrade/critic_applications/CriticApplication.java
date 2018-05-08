package centigrade.critic_applications;

import javax.persistence.*;

@Entity
@Table(name = "criticapplications")
public class CriticApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long account;
    private String sourceName;
    private String sourceUrl;
    private String resumeText;

    @Column(name="Id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="account")
    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    @Column(name="sourcename")
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Column(name="sourceurl")
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Column(name="resumetext")
    public String getResumeText() {
        return resumeText;
    }

    public void setResumeText(String resumeText) {
        this.resumeText = resumeText;
    }
}
