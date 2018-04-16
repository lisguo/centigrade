package centigrade.movies;

import centigrade.accounts.Account;
import centigrade.accounts.AccountService;
import centigrade.accounts.AccountType;
import centigrade.people.PersonService;
import centigrade.people.Person;

import java.text.DecimalFormat;
import java.util.*;

import centigrade.reviews.Review;
import centigrade.reviews.ReviewResult;
import centigrade.reviews.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

enum MovieSortCriteria {
    YEAR, TITLE, RATING, BOX_OFFICE
}

enum MovieSortDirection {
    ASCENDING, DESCENDING
}

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private PersonService personService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private Environment env;

    @GetMapping("/add_movie")
    public String addMovieForm(HttpSession session) {
        Account a = (Account) session.getAttribute("account");
        if(a == null || a.getAccountType() != AccountType.ADMIN){
            return "index";
        }
        return "add_movie";
    }

    @PostMapping("/add_movie")
    public @ResponseBody
    String addMovieSubmit(@RequestParam String title, @RequestParam String plot, HttpSession session) {
        Account a = (Account) session.getAttribute("account");
        if(a == null || a.getAccountType() != AccountType.ADMIN){
            return "index";
        }
        movieService.addMovie(title, plot);
        return "Saved";
    }

    @GetMapping("/movies")
    public String displayAllMoviesWithoutPageNum(Model model, @RequestParam(defaultValue = "TITLE") String sortBy,
                                              @RequestParam(defaultValue = "ASCENDING") String sortDirection) {
        return displayAllMovies( model,sortBy,sortDirection,1);
    }
    @GetMapping("/movies{pageNum}")
    public String displayAllMoviesWithPageNum(Model model, @RequestParam(defaultValue = "TITLE") String sortBy,
                                   @RequestParam(defaultValue = "ASCENDING") String sortDirection,@PathVariable("pageNum") String pageNum) {
    return displayAllMovies( model,sortBy,sortDirection,Integer.parseInt(pageNum));
    }
    public String displayAllMovies(Model model, String sortBy,
                String sortDirection,int page) {

        List<Movie> movies;
        String endLink ="?sortBy="+sortBy+"&sortDirection="+sortDirection;

        if (sortBy.equals("TITLE")) {
            movies = movieService.getAllMoviesSortedByTitle();
        } else if (sortBy.equals("YEAR")) {
            movies = movieService.getAllMoviesSortedByYear();
        } else { //rating or box office
            movies = movieService.getAllMovies();
        }

        for (Movie m : movies) {
            m.calculateOverallRating();
            m.calculateBoxOffice();
        }

        if(sortBy.equals("RATING")){
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie m1, Movie m2) {
                    if (m1.getOverallRating() > m2.getOverallRating()) {
                        return 1;
                    } else if (m1.getOverallRating() < m2.getOverallRating()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        }else if(sortBy.equals("BOX_OFFICE")){
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie m1, Movie m2) {
                    if (m1.getSortableBoxOffice() > m2.getSortableBoxOffice()) {
                        return 1;
                    } else if (m1.getSortableBoxOffice() < m2.getSortableBoxOffice()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        }

        if (sortDirection.equals("DESCENDING")) {
            Collections.reverse(movies);
        }
        List<Movie> outMovies = new ArrayList<Movie>();
        int searchAmount =Integer.parseInt(env.getProperty("num_search_results"));
        int end = page * searchAmount;
        int start = (page-1)*searchAmount;
        for(int i =start; i<end && i<movies.size();i++){
            outMovies.add(movies.get(i));
        }

        model.addAttribute("sortCriteria", EnumSet.allOf(MovieSortCriteria.class));
        model.addAttribute("sortDirections", EnumSet.allOf(MovieSortDirection.class));
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("movies", outMovies);
        if(page!=1) model.addAttribute("prev", "/movies"+(page-1)+endLink);
        if(end+1<movies.size())model.addAttribute("next", "/movies"+(page+1)+endLink);

        model.addAttribute("posterURL", movieService.getMoviePosterURL());
        DecimalFormat df = new DecimalFormat("#.##");
        model.addAttribute("decimalFormat", df);
        return "movies";
    }

    @GetMapping("/movie")
    public String displayMovie(@RequestParam long id, @RequestParam(required = false) ReviewResult res, Model model) {
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("posterURL", movieService.getMoviePosterURL());
        model.addAttribute("trailerURL", movieService.getMovieTrailerURL());
        model.addAttribute("photoURL", personService.getPersonPhotoURL());

        if (res == ReviewResult.SUCCESS) {
            model.addAttribute("message", env.getProperty("review_success"));
        } else if (res == ReviewResult.ALREADY_REVIEWED){
            model.addAttribute("message", env.getProperty("review_already_reviewed"));
        } else if (res == ReviewResult.DELETED){
            model.addAttribute("message", env.getProperty("review_deleted"));
        }

        List<Person> cast = personService.getCastByMovie(movie);
        model.addAttribute("cast", cast);
        List<Person> directors = personService.getDirectorsByMovie(movie);
        model.addAttribute("directors", directors);

        List<Review> reviews = reviewService.getReviewsByContent(id);
        ArrayList<Review> userReviews = new ArrayList<>();
        ArrayList<Review> criticReviews = new ArrayList<>();
        Account a;

        for (Review r : reviews) {
            a = accountService.getAccountById(r.getUserId());

            if(a == null) {
                continue;
            }

            r.setUserName(a.toString());

            if (a.getAccountType() == AccountType.CRITIC) {
                criticReviews.add(r);
            } else {
                userReviews.add(r);
            }
        }

        movie.calculateOverallRating();

        if (movie.getTimesRated() == 0) {
            model.addAttribute("rating", "Not Yet Rated");
        } else {
            model.addAttribute("rating", String.format("%.2f", movie.getOverallRating()) + "%");
        }

        model.addAttribute("reviewsCounted", movie.getTimesRated());
        model.addAttribute("userReviews", userReviews);
        model.addAttribute("criticReviews", criticReviews);

        return "movie";
    }

}
