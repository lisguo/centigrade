package centigrade.movies;

import centigrade.accounts.Account;
import centigrade.accounts.AccountService;
import centigrade.accounts.AccountType;
import centigrade.people.PersonService;
import centigrade.people.Person;

import java.util.ArrayList;
import java.util.List;

import centigrade.reviews.Review;
import centigrade.reviews.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/add_movie")
    public String addMovieForm(){
        return "add_movie";
    }

    @PostMapping("/add_movie")
    public @ResponseBody String addMovieSubmit (@RequestParam String title, @RequestParam String plot) {
        movieService.addMovie(title, plot);
        return "Saved";
    }

    @GetMapping("/movies")
    public String displayAllMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies"; // Show movie.html in templates
    }

    @GetMapping("/movie")
    public String displayMovie (@RequestParam long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("posterURL", movieService.getMoviePosterURL());
        model.addAttribute("trailerURL", movieService.getMovieTrailerURL());
        model.addAttribute("photoURL", personService.getPersonPhotoURL());

        List<Person> cast = personService.getCastByMovie(movie);
        model.addAttribute("cast", cast);
        List<Person> directors = personService.getDirectorsByMovie(movie);
        model.addAttribute("directors", directors);

        List<Review> reviews = reviewService.getReviewsByContent(id);
        ArrayList<Review> userReviews = new ArrayList<>();
        ArrayList<Review> criticReviews = new ArrayList<>();
        Account a;


        for(Review r : reviews){
            if(r.getReviewText() == null)
            {
                continue;
            }

            a = accountService.getAccountById(r.getUserId());
            r.setUserName(a.toString());

            if(a.getAccountType() == AccountType.CRITIC){
                criticReviews.add(r);
            }
            else{
                userReviews.add(r);
            }
        }

        movie.calculateOverallRating();

        if(movie.getTimesRated() == 0){
            model.addAttribute("rating", "Not Yet Rated");
        }else{
            model.addAttribute("rating", String.format("%.2f", movie.getOverallRating()) + "%");
        }

        model.addAttribute("reviewsCounted", movie.getTimesRated());
        model.addAttribute("userReviews", userReviews);
        model.addAttribute("criticReviews", criticReviews);

        return "movie";
    }

}
