package centigrade.TVShows;

import javax.persistence.*;

@Entity
@Table(name = "tvshows")
public class TVShow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "seriesname")
    private String seriesName;
    @Column(name = "contentrating")
    private String contentRating;
    @Column(name = "firstaired")
    private String firstAired;
    @Column(name = "numseasons")
    private int numSeasons;
    @Column(name = "genres")
    private String genres;
    @Column(name = "language")
    private String language;
    @Column(name = "network")
    private String network;
    @Column(name = "overview")
    private String overview;
    @Column(name = "runtime")
    private String runtime;
    @Column(name = "status")
    private String status;
    @Column(name = "ratingsum")
    private double ratingSum;
    @Column(name = "timesrated")
    private int timesRated;

    @Transient
    private double overallRating;

    public TVShow() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public double getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(double ratingSum) {
        this.ratingSum = ratingSum;
    }

    public int getTimesRated() {
        return timesRated;
    }

    public void setTimesRated(int timesRated) {
        this.timesRated = timesRated;
    }

    public void calculateOverallRating() {
        if (timesRated == 0) {
            overallRating = 0;
        } else {
            overallRating = ratingSum / (double) timesRated;
        }
    }

    public double getOverallRating() {
        return overallRating;
    }

    public boolean hasBeenRated() {
        if (timesRated > 0) {
            return true;
        }

        return false;
    }
}
