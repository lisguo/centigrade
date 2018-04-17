package centigrade.movies;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "movies")
public class Movie implements Comparator<Movie> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long id;
    private String title;
    private int year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String plot;
    private String boxoffice;
    private String production;
    private String website;
    private double ratingSum;
    private int timesRated;
    @Transient
    private double overallRating;
    @Transient
    private double sortableBoxOffice;

    public Movie() {
    }

    public Movie(long id, String title, String plot) {
        this.id = id;
        this.title = title;
        this.plot = plot;
    }

    @Column(name = "Id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "year")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Column(name = "rated")
    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    @Column(name = "released")
    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    @Column(name = "runtime")
    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    @Column(name = "genre")
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Column(name = "plot")
    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Column(name = "boxoffice")
    public String getBoxoffice() {
        return boxoffice;
    }

    public void setBoxoffice(String boxoffice) {
        this.boxoffice = boxoffice;
    }

    @Column(name = "production")
    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    @Column(name = "website")
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Column(name = "ratingsum")
    public double getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(double ratingSum) {
        this.ratingSum = ratingSum;
    }

    @Column(name = "timesrated")
    public int getTimesRated() {
        return timesRated;
    }

    public void setTimesRated(int timesRated) {
        this.timesRated = timesRated;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void calculateOverallRating() {
        if (timesRated == 0) {
            overallRating = 0;
        } else {
            overallRating = ratingSum / (double) timesRated;
        }
    }

    public boolean hasBeenRated() {
        if (timesRated > 0) {
            return true;
        }

        return false;
    }

    public double getSortableBoxOffice() {
        return sortableBoxOffice;
    }

    public void calculateBoxOffice(){
        if(this.boxoffice.equals("N/A")){
            this.sortableBoxOffice = 0;
            return;
        }

        String temp = this.boxoffice.replace("$", "").replace(",", "");
        this.sortableBoxOffice = Double.parseDouble(temp);
    }

    @Override
    public String toString() {
        return String.format("Movie[id=%d, title='%s', plot='%s']", id, title, plot);
    }

    @Override
    public int compare(Movie a, Movie b) {
        return a.title.compareTo(b.title);
    }

    public boolean equals(Movie m) {
        boolean out = true;
        if (this.id != m.id) {
            out = false;
        }

        return out;
    }
}
