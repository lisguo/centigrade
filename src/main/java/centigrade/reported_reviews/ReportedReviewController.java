package centigrade.reported_reviews;

import centigrade.TVShows.TVShow;
import centigrade.TVShows.TVShowService;
import centigrade.accounts.Account;
import centigrade.accounts.AccountService;
import centigrade.accounts.AccountType;
import centigrade.movies.Movie;
import centigrade.movies.MovieService;
import centigrade.reviews.Review;
import centigrade.reviews.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReportedReviewController {
    @Autowired
    Environment env;

    @Autowired
    private ReportedReviewService reportedReviewService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TVShowService tvShowService;

    @GetMapping("/get_reported_reviews")
    public String getAllReportedReviews(HttpSession session, Model model){
        Account a = (Account) session.getAttribute("account");
        if (a == null || a.getAccountType() != AccountType.ADMIN) {
            return "redirect:/";
        }

        List<ReportedReview> all = reportedReviewService.getAllReportedReviews();
        List<ReportedReview> reportedReviews = new ArrayList<>();

        for(ReportedReview r : all){
            Review rev = reviewService.getReviewById(r.getReviewid());
            if(rev != null){
                Account reviewer = accountService.getAccountById(rev.getUserId());
                if(reviewer == null){
                    continue;
                }

                Movie m = movieService.getMovieById(rev.getContentId());
                TVShow t = tvShowService.getTVShowById(rev.getContentId());

                if (m != null) {
                    rev.setContentName(m.getTitle());
                    rev.setContentType("Movie");
                } else if (t != null) {
                    rev.setContentName(t.getSeriesName());
                    rev.setContentType("Show");
                }

                rev.setUserName(reviewer.toString());
                r.setReview(rev);
                reportedReviews.add(r);
            }
            else{
                reportedReviewService.deleteReportedRevew(r);
            }
        }

        model.addAttribute("reportedReviews", reportedReviews);
        return "review_reported_reviews";
    }

    @PostMapping("/remove_reported_review")
    public String removeReportedReview(HttpSession session, @RequestParam long id, Model model){
        Account a = (Account) session.getAttribute("account");
        if (a == null || a.getAccountType() != AccountType.ADMIN) {
            return "redirect:/";
        }

        ReportedReview rr = reportedReviewService.getReportedReviewById(id);
        reportedReviewService.deleteReportedRevew(rr);

        String successMsg = env.getProperty("remove_reported_review_success");
        model.addAttribute("notificationTitle", "Success!");
        model.addAttribute("notificationDetails", successMsg);

        return "notification_alert";
    }
}
