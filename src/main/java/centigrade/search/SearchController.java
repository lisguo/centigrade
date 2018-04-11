package centigrade.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import centigrade.movies.Movie;
import centigrade.TVShows.TVShow;
import centigrade.TVShows.TVShowService;
import centigrade.people.PersonService;
import centigrade.movies.MovieService;

import javax.validation.constraints.Null;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ListIterator;
import java.util.Comparator;
import java.util.ArrayList;

@Controller
public class SearchController {

    @Autowired
    private PersonService personService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TVShowService tvShowService;

    @Autowired
    private Environment env;

    @GetMapping("/search")
    public String displaySearch(@RequestParam String search, Model model) {
        model.addAttribute("moviePosterURL", movieService.getMoviePosterURL());
        model.addAttribute("showPosterURL", tvShowService.getTVShowPosterURL());
        if (search.replaceAll("\\s+", "").length() == 0) {
            return "searchResults";
        }


        List<Movie> movies = getSearchMovies(search);
        if (movies.size() > 0) {
            model.addAttribute("movies", movies);
        }
        List<TVShow> shows = getSearchTVShows(search);
        if (shows.size() > 0) {
            model.addAttribute("shows", shows);
        }


        return "searchResults";
    }

    private List<TVShow> getSearchTVShows(String search) {
        String[] splited = search.split("\\s+");
        List<TVShow> shows = new ArrayList<>();

        for (int i = 0; i < splited.length; i++) {
            if (isCommonWord(splited[i])) {
                continue;
            }


            List<TVShow> list = tvShowService.getLikeShows(splited[i]);
            shows.addAll(list);
        }
        shows.sort(new Comparator<TVShow>() {
            @Override
            public int compare(TVShow s1, TVShow s2) {
                return (int) (s1.getId() - s2.getId());
            }
        });

        shows = getTopMatchShows(shows, 10);
        return shows;

    }

    private List<TVShow> getTopMatchShows(List<TVShow> shows, int n) {
        if (shows.size() <= 0) {

            return shows;
        }
        List<TVShow> list = new ArrayList<TVShow>();
        for (int i = 0; i < Integer.parseInt(env.getProperty("num_search_results")); i++) {
            TVShow topShow = get_max_show(shows);
            if (topShow == null) {
                break;
            }
            list.add(topShow);
        }
        return list;

    }

    private TVShow get_max_show(List<TVShow> shows) {
        if (shows.size() <= 0) {

            return null;
        }
        TVShow temp = shows.get(0);
        int count = 1;
        int maxCount = 1;
        TVShow maxShow = temp;
        TVShow Cursor;
        for (int i = 1; i < shows.size(); i++) {
            Cursor = shows.get(i);
            if (temp.equals(Cursor)) {
                count++;
            } else {
                if (maxCount < count) {
                    maxShow = temp;
                    maxCount = count;
                }
                temp = Cursor;
                count = 1;
            }
        }
        int i = 0;
        while (i < shows.size()) {
            Cursor = shows.get(i);
            if (maxShow.equals(Cursor)) {
                shows.remove(i);
            } else {
                i++;
            }
        }
        return maxShow;
    }

    private List<Movie> getSearchMovies(String search) {
        String[] splited = search.split("\\s+");
        List<Movie> movies = new ArrayList();

        for (int i = 0; i < splited.length; i++) {
            if (isCommonWord(splited[i])) {
                continue;
            }

            List<Movie> list = movieService.getLikeMovies(splited[i]);
            movies.addAll(list);
        }
        movies.sort(new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return (int) (m1.getId() - m2.getId());
            }
        });

        movies = getTopMatchMovies(movies, 10);
        return movies;

    }

    private List<Movie> getTopMatchMovies(List<Movie> movies, int n) {
        if (movies.size() <= 0) {

            return movies;
        }
        List<Movie> list = new ArrayList<Movie>();
        for (int i = 0; i < Integer.parseInt(env.getProperty("num_search_results")); i++) {
            Movie topMovie = get_max_movie(movies);
            if (topMovie == null) {
                break;
            }
            list.add(topMovie);
        }
        return list;

    }

    private Movie get_max_movie(List<Movie> movies) {
        if (movies.size() <= 0) {

            return null;
        }
        Movie temp = movies.get(0);
        int count = 1;
        int maxCount = 1;
        Movie maxMovie = temp;
        Movie Cursor;
        for (int i = 1; i < movies.size(); i++) {
            Cursor = movies.get(i);
            if (temp.equals(Cursor)) {
                count++;
            } else {
                if (maxCount < count) {
                    maxMovie = temp;
                    maxCount = count;
                }
                temp = Cursor;
                count = 1;
            }
        }
        int i = 0;
        while (i < movies.size()) {
            Cursor = movies.get(i);
            if (maxMovie.equals(Cursor)) {
                movies.remove(i);
            } else {
                i++;
            }
        }
        return maxMovie;
    }

    private boolean isCommonWord(String s) {
        String word = s.toLowerCase().replaceAll("\\s+", "");
        if (word.length() == 0) {
            return true;
        }
        String[] commonWords = env.getProperty("common_words").split(",");
//        String[] commonWords = {"the", "of", "in", "or", "and", "if", "is", "a", ""};
        for (int i = 0; i < commonWords.length; i++) {
            if (word.equals(commonWords[i])) {
                return true;
            }
        }
        return false;
    }
}
