package centigrade.TVShows;

import javax.persistence.*;

@Entity
@Table(name = "tvshows")
public class TVShow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long id;
    private String seriesName;
    private String contentRating;
    private String firstAired;
    private int numSeasons;
    private String genres;
    private String language;
    private String network;
    private String overview;
    private String runtime;
    private String status;
    private double ratingSum;
    private int timesRated;
    @Transient
    private double overallRating;

    public TVShow() {
    }

    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "seriesname")
    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    @Column(name = "contentrating")
    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    @Column(name = "firstaired")
    public String getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    @Column(name = "numseasons")
    public int getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    @Column(name = "genres")
    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Column(name = "network")
    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @Column(name = "overview")
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Column(name = "runtime")
    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
