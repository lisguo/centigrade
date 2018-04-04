package centigrade.movies;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "movies")
public class Movie implements Comparator<Movie> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "year")
    private int year;
    @Column(name = "rated")
    private String rated;
    @Column(name = "released")
    private String released;
    @Column(name = "runtime")
    private String runtime;
    @Column(name = "genre")
    private String genre;
    @Column(name = "plot")
    private String plot;
    @Column(name = "boxoffice")
    private String boxoffice;
    @Column(name = "production")
    private String production;
    @Column(name = "website")
    private String website;
    @Column(name = "trailerurl")
    private String trailerurl;

    public Movie() {
    }

    public Movie(long id, String title, String plot) {
        this.id = id;
        this.title = title;
        this.plot = plot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public String toString() {
        return String.format("Movie[id=%d, title='%s', plot='%s']", id, title, plot);
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTrailerurl() {
        return trailerurl;
    }

    public void setTrailerurl(String trailerurl) {
        this.trailerurl = trailerurl;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getBoxoffice() {
        return boxoffice;
    }

    public void setBoxoffice(String boxoffice) {
        this.boxoffice = boxoffice;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int compare(Movie a, Movie b) {
        return a.title.compareTo(b.title);
    }
    public boolean equals(Movie m){
        boolean out = true;
        if(this.id!= m.id){
            out = false;
        }
//        if(this.title.equals(m.title)){
//            out = false;
//        }
//        if(this.year == (m.year)){
//            out = false;
//        }
//        if(this.rated.equals(m.rated)){
//            out = false;
//        }
//        if(this.released.equals(m.released)){
//            out = false;
//        }
//        if(this.runtime.equals(m.runtime)){
//            out = false;
//        }
//        if(this.genre.equals(m.genre)){
//            out = false;
//        }
//        if(this.plot.equals(m.plot)){
//            out = false;
//        }
//        if(this.boxoffice.equals(m.boxoffice)){
//            out = false;
//        }
//        if(this.production.equals(m.production)){
//            out = false;
//        }
//        if(this.website.equals(m.website)){
//            out = false;
//        }
//        if(this.trailerurl.equals(m.trailerurl)){
//            out = false;
//        }
        return out;
    }
}
