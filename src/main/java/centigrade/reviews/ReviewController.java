package centigrade.reviews;

import centigrade.TVShows.TVShow;
import centigrade.TVShows.TVShowService;
import centigrade.accounts.Account;
import centigrade.movies.Movie;
import centigrade.movies.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ReviewController {

    @Autowired
    private Environment env;

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private TVShowService tvShowService;

    @PostMapping("add_review")
    public String addReview(@RequestParam String reviewtext, @RequestParam double rating,
                            @RequestParam String contentType, @RequestParam long contentID, HttpSession session) {
        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            return "login";
        }

        if (reviewtext.trim().equals("Add Review (Optional)")) {
            reviewtext = null;
        }

        reviewService.addReview(contentID, a.getId(), rating, reviewtext);

        if (contentType.equals("Movie")) {
            Movie m = movieService.getMovieById(contentID);
            m.setRatingSum(m.getRatingSum() + rating);
            m.setTimesRated(m.getTimesRated() + 1);
            movieService.saveMovie(m);
        } else {
            TVShow t = tvShowService.getTVShowById(contentID);
            t.setRatingSum(t.getRatingSum() + rating);
            t.setTimesRated(t.getTimesRated() + 1);
            tvShowService.saveShow(t);
        }

        return "index";
    }
}
