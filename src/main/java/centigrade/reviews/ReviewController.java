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
                                  @RequestParam long contentID, HttpSession session) {
        RedirectView rv = new RedirectView();
        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            rv.setUrl(("login"));
            return rv;
        }

        Movie m = movieService.getMovieById(contentID);
        TVShow t = tvShowService.getTVShowById(contentID);

        List<Review> reviews = reviewService.getReviewsByUserAndContent(a.getId(), contentID);
        if (reviews.size() > 0) {
            if (m != null) {
                rv.setUrl("movie?id=" + contentID + "&res=" + ReviewResult.ALREADY_REVIEWED);
            } else if (t != null){
                rv.setUrl("show?id=" + contentID + "&res=" + ReviewResult.ALREADY_REVIEWED);
            }
            return rv;
        }

        if (reviewtext.trim().equals("Add Review (Optional)")) {
            reviewtext = null;
        }

        reviewService.addReview(contentID, a.getId(), rating, reviewtext);

        if (m != null) {
            m.setRatingSum(m.getRatingSum() + rating);
            m.setTimesRated(m.getTimesRated() + 1);
            movieService.saveMovie(m);
            rv.setUrl("movie?id=" + contentID + "&res=" + ReviewResult.SUCCESS);
        } else if(t != null){
            t.setRatingSum(t.getRatingSum() + rating);
            t.setTimesRated(t.getTimesRated() + 1);
            tvShowService.saveShow(t);
            rv.setUrl("show?id=" + contentID + "&res=" + ReviewResult.SUCCESS);
        }
        return rv;
    }

    @PostMapping("delete_review")
    public RedirectView deleteReview(@RequestParam long id, @RequestParam(required = false) String fromProfile,
                                     @RequestParam(required = false) String fromAdmin) {
        Review r = reviewService.getReviewById(id);

        RedirectView rv = new RedirectView();
        Movie m = movieService.getMovieById(r.getContentId());
        TVShow t = tvShowService.getTVShowById(r.getContentId());

        if(fromAdmin != null){
            rv.setUrl("admin");
        } else if (fromProfile != null) {
            rv.setUrl("profile?id=" + r.getUserId());
        } else if (m != null) {
            rv.setUrl("movie?id=" + r.getContentId() + "&res=" + ReviewResult.DELETED);
        } else if (t != null) {
            rv.setUrl("show?id=" + r.getContentId() + "&res=" + ReviewResult.DELETED);
        }

        reviewService.deleteReview(r);
        return rv;
    }

    @GetMapping("edit_review")
    public String editReviewForm(@RequestParam long id, HttpSession session, Model model,
                                 @RequestParam(required = false) String fromProfile){
        Review r = reviewService.getReviewById(id);

        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            return "login";
        }else if(a.getId() != r.getUserId()){
            return "error";
        }

        Movie m = movieService.getMovieById(r.getContentId());
        TVShow t = tvShowService.getTVShowById(r.getContentId());
        if(m != null) {
            r.setContentName(m.getTitle());
        } else if(t != null) {
            r.setContentName(t.getSeriesName());
        }

        model.addAttribute("review", r);
        model.addAttribute("fromProfile", fromProfile);
        return "edit_review";
    }

    @PostMapping("edit_review")
    public RedirectView editReviewSubmit(@RequestParam long id, @RequestParam String reviewText,
                                         @RequestParam double rating, HttpSession session,
                                         @RequestParam(required = false) String fromProfile){
        Review r = reviewService.getReviewById(id);
        RedirectView rv = new RedirectView();

        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            rv.setUrl(("login"));
            return rv;
        }else if(a.getId() != r.getUserId()){
            rv.setUrl("error");
            return rv;
        }

        Movie m = movieService.getMovieById(r.getContentId());
        TVShow t = tvShowService.getTVShowById(r.getContentId());

        if (m != null) {
            m.setRatingSum(m.getRatingSum() - r.getRating());
            m.setRatingSum(m.getRatingSum() + rating);
            movieService.saveMovie(m);
            rv.setUrl("movie?id=" + r.getContentId() + "&res=" + ReviewResult.EDITED);
        } else if (t != null){
            t.setRatingSum(t.getRatingSum() - r.getRating());
            t.setRatingSum(t.getRatingSum() + rating);
            tvShowService.saveShow(t);
            rv.setUrl("show?id=" + r.getContentId() + "&res=" + ReviewResult.EDITED);
        }

        reviewService.editReview(r, rating, reviewText);
        if(fromProfile != null){
            rv.setUrl("profile?id=" + r.getUserId());
        }

        return rv;
    }

    @GetMapping("report_review")
    public String reportPage(HttpSession session, long id, Model model, @RequestParam(required = false) String fromProfile){
        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            return "login";
        }
        Review r = reviewService.getReviewById(id);
        model.addAttribute("review", r);
        model.addAttribute("fromProfile", fromProfile);

        return "report_review";
    }

    @PostMapping("report_review")
    public RedirectView reportReview(@RequestParam String message,
                                     HttpSession session,
                                     @RequestParam long id,
                                     @RequestParam(required = false) String fromProfile){
        RedirectView rv = new RedirectView();

        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            rv.setUrl(("login"));
            return rv;
        }

        Review r = reviewService.getReviewById(id);
        Movie m = movieService.getMovieById(r.getContentId());
        TVShow t = tvShowService.getTVShowById(r.getContentId());

        if (m != null) {
            rv.setUrl("movie?id=" + r.getContentId());
        } else if (t != null){
            rv.setUrl("show?id=" + r.getContentId());
        }

        if(fromProfile != null){
            rv.setUrl("profile?id=" + r.getUserId());
        }

        reviewService.reportReview(id, message, a.getId());
        return rv;
    }
}
