package centigrade.search;

import centigrade.people.Person;
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
        model.addAttribute("search", search);
        return "searchResults";
    }

    @GetMapping("/search_movies")
    private String  getSearchMovies(String search, Model model) {
        model.addAttribute("moviePosterURL", movieService.getMoviePosterURL());

        String[] splitted = search.split("\\s+");
        List<Movie> movies = new ArrayList();

        for (int i = 0; i < splitted.length; i++) {
            if (isCommonWord(splitted[i])) {
                continue;
            }

            List<Movie> list = movieService.getLikeMovies(splitted[i]);
            movies.addAll(list);
        }
        movies.sort(new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2)
            {
                return (int) (m1.getId() - m2.getId());
            }
        });


        int numResults = Integer.parseInt(env.getProperty("num_search_results"));
        movies = getTopMatchMovies(movies, numResults);

        model.addAttribute("movies", movies);
        return "search_movies";
    }

    @GetMapping("/search_shows")
    private String getSearchTVShows(String search, Model model) {
        model.addAttribute("showPosterURL", tvShowService.getTVShowPosterURL());

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

        int numResults = Integer.parseInt(env.getProperty("num_search_results"));
        shows = getTopMatchShows(shows, numResults);

        model.addAttribute("shows", shows);
        return "search_shows";
    }

    @GetMapping("search_people")
    private String getSearchPeople(String search, Model model) {
        model.addAttribute("personPhotoURL", personService.getPersonPhotoURL());

        String[] splitted = search.split("\\s+");
        List<Person> people = new ArrayList();

        for (int i = 0; i < splitted.length; i++) {
            if (isCommonWord(splitted[i])) {
                continue;
            }

            List<Person> list = personService.getLikePeople(splitted[i]);
            people.addAll(list);
        }
        people.sort(new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2)
            {
                return (int) (p1.getId() - p2.getId());
            }
        });

        int numResults = Integer.parseInt(env.getProperty("num_search_results"));
        people = getTopMatchPeople(people, numResults);

        model.addAttribute("people", people);
        return "search_people";
    }

    private List<TVShow> getTopMatchShows(List<TVShow> shows, int n) {
        if (shows.size() <= 0) {
            return shows;
        }
        List<TVShow> list = new ArrayList<TVShow>();
        for (int i = 0; i < Integer.parseInt(env.getProperty("num_search_results")); i++) {
            TVShow topShow = getMaxShow(shows);
            if (topShow == null) {
                break;
            }
            list.add(topShow);
        }
        return list;
    }

    private TVShow getMaxShow(List<TVShow> shows) {
        if (shows.size() <= 0) {

            return null;
        }
        TVShow temp = shows.get(0);
        int count = 1;
        int maxCount = 1;
        TVShow maxShow = temp;
        TVShow cursor;
        for (int i = 1; i < shows.size(); i++) {
            cursor = shows.get(i);
            if (temp.equals(cursor)) {
                count++;
            } else {
                if (maxCount < count) {
                    maxShow = temp;
                    maxCount = count;
                }
                temp = cursor;
                count = 1;
            }
        }
        int i = 0;
        while (i < shows.size()) {
            cursor = shows.get(i);
            if (maxShow.equals(cursor)) {
                shows.remove(i);
            } else {
                i++;
            }
        }
        return maxShow;
    }

    private List<Movie> getTopMatchMovies(List<Movie> movies, int n) {
        if (movies.size() <= 0) {
            return movies;
        }
        List<Movie> list = new ArrayList<Movie>();
        for (int i = 0; i < Integer.parseInt(env.getProperty("num_search_results")); i++) {
            Movie topMovie = getMaxMovie(movies);
            if (topMovie == null) {
                break;
            }
            list.add(topMovie);
        }
        return list;
    }

    private Movie getMaxMovie(List<Movie> movies) {
        if (movies.size() <= 0) {
            return null;
        }
        Movie temp = movies.get(0);
        int count = 1;
        int maxCount = 1;
        Movie maxMovie = temp;
        Movie cursor;
        for (int i = 1; i < movies.size(); i++) {
            cursor = movies.get(i);
            if (temp.equals(cursor)) {
                count++;
            } else {
                if (maxCount < count) {
                    maxMovie = temp;
                    maxCount = count;
                }
                temp = cursor;
                count = 1;
            }
        }
        int i = 0;
        while (i < movies.size()) {
            cursor = movies.get(i);
            if (maxMovie.equals(cursor)) {
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

        for (int i = 0; i < commonWords.length; i++) {
            if (word.equals(commonWords[i])) {
                return true;
            }
        }
        return false;
    }

    private List<Person> getTopMatchPeople(List<Person> people, int n) {
        if (people.size() <= 0) {
            return people;
        }
        List<Person> list = new ArrayList<Person>();
        for (int i = 0; i < Integer.parseInt(env.getProperty("num_search_results")); i++) {
            Person topPerson = getMaxPerson(people);
            if (topPerson == null) {
                break;
            }
            list.add(topPerson);
        }
        return list;
    }

    private Person getMaxPerson(List<Person> people) {
        if (people.size() <= 0) {
            return null;
        }
        Person temp = people.get(0);
        int count = 1;
        int maxCount = 1;
        Person maxPerson = temp;
        Person cursor;
        for (int i = 1; i < people.size(); i++) {
            cursor = people.get(i);
            if (temp.equals(cursor)) {
                count++;
            } else {
                if (maxCount < count) {
                    maxPerson = temp;
                    maxCount = count;
                }
                temp = cursor;
                count = 1;
            }
        }
        int i = 0;
        while (i < people.size()) {
            cursor = people.get(i);
            if (maxPerson.equals(cursor)) {
                people.remove(i);
            } else {
                i++;
            }
        }
        return maxPerson;
    }
}
