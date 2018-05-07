package centigrade.TVShows;

import centigrade.Genre;
import centigrade.accounts.WishListItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tvshows")
public class TVShow implements WishListItem{
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
    @Transient
    private List<Genre> genreEnums;

    public TVShow() {
    }

    @Column(name = "id")
    public long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return getSeriesName();
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

    @Override
    public String getType() {
        return "show";
    }

    public void fillGenreEnums(){
        String[] genreList = genres.split("\\s+");
        genreEnums = new ArrayList<>();

        for(String genre : genreList){
            String g = genre.replace(",", "");
            if(g.equals("Animation")){
                genreEnums.add(Genre.ANIMATION);
            } else if(g.equals("Comedy")){
                genreEnums.add(Genre.COMEDY);
            }else if(g.equals("Food")){
                genreEnums.add(Genre.FOOD);
            }else if(g.equals("Drama")){
                genreEnums.add(Genre.DRAMA);
            }else if(g.equals("Family")){
                genreEnums.add(Genre.FAMILY);
            }else if(g.equals("Documentary")){
                genreEnums.add(Genre.DOCUMENTARY);
            }else if(g.equals("News")){
                genreEnums.add(Genre.NEWS);
            }else if(g.equals("Reality")){
                genreEnums.add(Genre.REALITY);
            }else if(g.equals("Adventure")){
                genreEnums.add(Genre.ADVENTURE);
            }else if(g.equals("Fantasy")){
                genreEnums.add(Genre.FANTASY);
            }else if(g.equals("Children")){
                genreEnums.add(Genre.CHILDREN);
            }else if(g.equals("Game")){
                genreEnums.add(Genre.GAME_SHOW);
            }else if(g.equals("Action")){
                genreEnums.add(Genre.ACTION);
            }else if(g.equals("Horror")){
                genreEnums.add(Genre.HORROR);
            }else if(g.equals("Sport")){
                genreEnums.add(Genre.SPORT);
            }else if(g.equals("Travel")){
                genreEnums.add(Genre.TRAVEL);
            }else if(g.equals("Crime")){
                genreEnums.add(Genre.CRIME);
            }else if(g.equals("Thriller")){
                genreEnums.add(Genre.THRILLER);
            } else if(g.equals("Mini-Series")){
                genreEnums.add(Genre.MINI_SERIES);
            }else if(g.equals("Science-Fiction")){
                genreEnums.add(Genre.SCI_FI);
            }else if(g.equals("Home")){
                genreEnums.add(Genre.HOME_AND_GARDEN);
            }else if(g.equals("Mystery")){
                genreEnums.add(Genre.MYSTERY);
            }else if(g.equals("Romance")){
                genreEnums.add(Genre.ROMANCE);
            }else if(g.equals("Soap")){
                genreEnums.add(Genre.SOAP);
            }else if(g.equals("Talk")){
                genreEnums.add(Genre.TALK);
            }else if(g.equals("Western")){
                genreEnums.add(Genre.WESTERN);
            }else if(g.equals("Suspense")){
                genreEnums.add(Genre.SUSPENSE);
            }
        }

    }

    public List<Genre> getGenreEnums() {
        return genreEnums;
    }

    public void setGenreEnums(List<Genre> genreEnums) {
        this.genreEnums = genreEnums;
    }
}
