package centigrade.movies;

import centigrade.Genre;
import centigrade.accounts.WishListItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie implements Comparator<Movie>, WishListItem {
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
    @Transient
    private List<Genre> genreEnums;

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

    @Override
    public String getType() {
        return "movie";
    }

    public void fillGenreEnums(){
        String[] genres = genre.split("\\s+");
        genreEnums = new ArrayList<>();

        for(String genre : genres){
            String g = genre.replace(",", "");
            if(g.equals("Drama")){
                genreEnums.add(Genre.DRAMA);
            }else if(g.equals("Musical")){
                genreEnums.add(Genre.MUSICAL);
            }else if(g.equals("Romance")){
                genreEnums.add(Genre.ROMANCE);
            }else if(g.equals("Family")){
                genreEnums.add(Genre.FAMILY);
            }else if(g.equals("War")){
                genreEnums.add(Genre.WAR);
            }else if(g.equals("Crime")){
                genreEnums.add(Genre.CRIME);
            }else if(g.equals("Thriller")){
                genreEnums.add(Genre.THRILLER);
            }else if(g.equals("Action")){
                genreEnums.add(Genre.ACTION);
            }else if(g.equals("Adventure")){
                genreEnums.add(Genre.ADVENTURE);
            }else if(g.equals("Comedy")){
                genreEnums.add(Genre.COMEDY);
            }else if(g.equals("History")){
                genreEnums.add(Genre.HISTORY);
            }else if(g.equals("Biography")){
                genreEnums.add(Genre.BIOGRAPHY);
            }else if(g.equals("Mystery")){
                genreEnums.add(Genre.MYSTERY);
            }else if(g.equals("Documentary")){
                genreEnums.add(Genre.DOCUMENTARY);
            }else if(g.equals("Short")){
                genreEnums.add(Genre.SHORT);
            }else if(g.equals("Sport")){
                genreEnums.add(Genre.SPORT);
            }else if(g.equals("Western")){
                genreEnums.add(Genre.WESTERN);
            }else if(g.equals("Fantasy")){
                genreEnums.add(Genre.FANTASY);
            }else if(g.equals("Sci-Fi")){
                genreEnums.add(Genre.SCI_FI);
            }else if(g.equals("Horror")){
                genreEnums.add(Genre.HORROR);
            }else if(g.equals("Adult")){
                genreEnums.add(Genre.ADULT);
            }else if(g.equals("Animation")){
                genreEnums.add(Genre.ANIMATION);
            }else if(g.equals("Music")){
                genreEnums.add(Genre.MUSIC);
            }else if(g.equals("Film-Noir")){
                genreEnums.add(Genre.FILM_NOIR);
            }else if(g.equals("News")){
                genreEnums.add(Genre.NEWS);
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
