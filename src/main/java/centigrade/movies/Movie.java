package centigrade.movies;

import javax.persistence.*;

@Entity
@Table(name = "Movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "summary")
    private String summary;

    public Movie(){}

    public Movie(long id, String title, String summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return String.format("Movie[id=%d, title='%s', summary='%s']", id, title, summary);
    }
}
