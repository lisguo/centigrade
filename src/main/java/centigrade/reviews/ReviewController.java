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
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

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
    public RedirectView addReview(@RequestParam String reviewtext, @RequestParam double rating,
                                  @RequestParam String contentType, @RequestParam long contentID,
                                  HttpSession session) {

        RedirectView rv = new RedirectView();
        Account a = (Account) session.getAttribute("account");

        if (a == null) {
            rv.setUrl(("login"));
            return rv;
        }

        List<Review> reviews = reviewService.getReviewsByUserAndContent(a.getId(), contentID);

        if (reviews.size() > 0) {
            if (contentType.equals("Movie")) {
                rv.setUrl("movie?id=" + contentID + "&res=" + ReviewResult.ALREADY_REVIEWED);
            } else {
                rv.setUrl("show?id=" + contentID + "&res=" + ReviewResult.ALREADY_REVIEWED);
            }

            return rv;
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
            rv.setUrl("movie?id=" + contentID + "&res=" + ReviewResult.SUCCESS);

        } else {
            TVShow t = tvShowService.getTVShowById(contentID);
            t.setRatingSum(t.getRatingSum() + rating);
            t.setTimesRated(t.getTimesRated() + 1);
            tvShowService.saveShow(t);
            rv.setUrl("show?id=" + contentID + "&res=" + ReviewResult.SUCCESS);
        }

        return rv;
    }

    @PostMapping("delete_review")
    public RedirectView deleteReview(@RequestParam long id, @RequestParam(required = false) String fromProfile) {
        Review r = reviewService.getReviewById(id);
        RedirectView rv = new RedirectView();

        Movie m = movieService.getMovieById(r.getContentId());
        TVShow t = tvShowService.getTVShowById(r.getContentId());

        if (fromProfile != null) {
            rv.setUrl("profile?id=" + r.getUserId());
        } else if (m != null) {
            rv.setUrl("movie?id=" + r.getContentId() + "&res=" + ReviewResult.DELETED);
        } else if (t != null) {
            rv.setUrl("show?id=" + r.getContentId() + "&res=" + ReviewResult.DELETED);
        }

        reviewService.deleteReview(r);
        return rv;
    }
}
