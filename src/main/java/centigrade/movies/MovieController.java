package centigrade.movies;

import centigrade.accounts.Account;
import centigrade.accounts.AccountService;
import centigrade.accounts.AccountType;
import centigrade.accounts.WishListResult;
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
    YEAR, TITLE, RATING, BOX_OFFICE,POPULAR
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

    @GetMapping("/movies")
    public String displayMovies(Model model,
                                @RequestParam(defaultValue = "TITLE")String sortBy,
                                @RequestParam(defaultValue = "ASCENDING") String sortDirection){
        model.addAttribute("sortCriteria", EnumSet.allOf(MovieSortCriteria.class));
        model.addAttribute("sortDirections", EnumSet.allOf(MovieSortDirection.class));
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        return "movies";
    }

    @GetMapping("/movies_table")
    public String displayAllMovies(Model model, @RequestParam(defaultValue = "TITLE")String sortBy,
                                   @RequestParam(defaultValue = "ASCENDING") String sortDirection,
                                   @RequestParam(defaultValue = "1") int page) {

        List<Movie> movies;

        if (sortBy.equals("TITLE")) {
            movies = movieService.getAllMoviesSortedByTitle();
        } else if (sortBy.equals("YEAR")) {
            movies = movieService.getAllMoviesSortedByYear();
        } else
            {
            movies = movieService.getAllMovies();
        }

        for (Movie m : movies) {
            m.calculateOverallRating();
            m.calculateBoxOffice();
        }

        if (sortBy.equals("RATING")) {
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
        } else if (sortBy.equals("BOX_OFFICE")) {
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
        }else if (sortBy.equals("POPULAR")) {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie m1, Movie m2) {

                    if (m1.getOverallRating() > m2.getOverallRating()) {
                        return 1;
                    } else if (m1.getOverallRating() < m2.getOverallRating()) {
                        return -1;
                    } else {
                        if (m1.getTimesRated() > m2.getTimesRated()) {
                            return 1;
                        } else if (m1.getTimesRated() < m2.getTimesRated()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
            });
        }

        if (sortDirection.equals("DESCENDING")) {
            Collections.reverse(movies);
        }

        List<Movie> outMovies = new ArrayList<Movie>();
        int searchAmount = Integer.parseInt(env.getProperty("num_search_results"));
        int end = page * searchAmount;
        int start = (page - 1) * searchAmount;
        for (int i = start; i < end && i < movies.size(); i++) {
            outMovies.add(movies.get(i));
        }

        model.addAttribute("sortCriteria", EnumSet.allOf(MovieSortCriteria.class));
        model.addAttribute("sortDirections", EnumSet.allOf(MovieSortDirection.class));
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("movies", outMovies);

        if (page != 1){
            model.addAttribute("prev", "1");
        }
        if (end + 1 < movies.size()){
            model.addAttribute("next", "1");
        }

        model.addAttribute("posterURL", movieService.getMoviePosterURL());
        DecimalFormat df = new DecimalFormat("#.##");
        model.addAttribute("decimalFormat", df);
        return "movies_table";
    }

    @GetMapping("/best_pictures")
    public String bestPictures(Model model){
        List<Movie> movies = movieService.getBestPictureWinners();
        Collections.reverse(movies);
        for (Movie m : movies) {
            m.calculateOverallRating();
            m.calculateBoxOffice();
        }
        model.addAttribute("posterURL", movieService.getMoviePosterURL());
        DecimalFormat df = new DecimalFormat("#.##");
        model.addAttribute("decimalFormat", df);
        model.addAttribute("movies", movies);
        return "best_pictures";
    }

    @GetMapping("/movie")
    public String displayMovie(@RequestParam long id,
                               @RequestParam(required = false) ReviewResult res,
                               @RequestParam(required = false) WishListResult notInterested,
                               @RequestParam(required = false) WishListResult wishList, Model model) {
        Movie movie = movieService.getMovieById(id);

        model.addAttribute("movie", movie);
        model.addAttribute("posterURL", movieService.getMoviePosterURL());
        model.addAttribute("trailerURL", movieService.getMovieTrailerURL());
        model.addAttribute("photoURL", personService.getPersonPhotoURL());
        model.addAttribute("profilePicURL", accountService.getUserPhotoURL());

        if (res == ReviewResult.SUCCESS) {
            model.addAttribute("message", env.getProperty("review_success"));
        } else if (res == ReviewResult.ALREADY_REVIEWED) {
            model.addAttribute("message", env.getProperty("review_already_reviewed"));
        } else if (res == ReviewResult.DELETED) {
            model.addAttribute("message", env.getProperty("review_deleted"));
        } else if(res == ReviewResult.EDITED) {
            model.addAttribute("message", env.getProperty("review_edited"));
        }

        if(wishList == WishListResult.ADDED) {
            model.addAttribute("message", env.getProperty("wishlist_added"));
        }
        else if(wishList == WishListResult.EXISTS){
            model.addAttribute("message", env.getProperty("wishlist_exists"));
        }

        if(notInterested == WishListResult.ADDED) {
            model.addAttribute("message", env.getProperty("not_interested_added"));
        }
        else if(notInterested == WishListResult.EXISTS){
            model.addAttribute("message", env.getProperty("not_interested_exists"));
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
            if (a == null) {
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
            model.addAttribute("rating", String.format("%.2f", -1.0) + "%");
        } else {
            model.addAttribute("rating", String.format("%.2f", movie.getOverallRating()) + "%");
        }

        model.addAttribute("reviewsCounted", movie.getTimesRated());

        Collections.reverse(userReviews);
        Collections.reverse(criticReviews);
        model.addAttribute("userReviews", userReviews);
        model.addAttribute("criticReviews", criticReviews);

        return "movie";
    }

    @PostMapping("/delete_movie")
    public String deleteMovie(long movieId, Model model){
        List<Review> reviews = reviewService.getReviewsByContent(movieId);
        for(Review r : reviews){
            reviewService.deleteReview(r);
        }

        Movie m = movieService.getMovieById(movieId);
        movieService.deleteMovie(m);

        String successMsg = env.getProperty("delete_movie_success");
        model.addAttribute("notificationTitle", "Success!");
        model.addAttribute("notificationDetails", successMsg);

        return "notification_alert";
    }
}