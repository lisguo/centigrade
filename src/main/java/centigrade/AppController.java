package centigrade;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class AppController {

    @Value("${app_name}")
    private String appName;

    @Autowired
    private Environment env;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private TVShowService tvShowService;
    @Autowired
    private AccountService accountService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("appName", this.appName);

        List<Review> reviews = reviewService.getAllReviewsOrderedById();
        Collections.reverse(reviews);

        List<Review> latestReviews = new ArrayList<>();

        for(Review r : reviews){
            Account a = accountService.getAccountById(r.getUserId());
            if (a == null) {
                continue;
            }
            if (a.getAccountType() != AccountType.CRITIC) {
                continue;
            }

            r.setUserName(a.toString());

            Movie m = movieService.getMovieById(r.getContentId());
            TVShow t = tvShowService.getTVShowById(r.getContentId());

            if (m != null) {
                r.setContentName(m.getTitle());
                r.setContentType("Movie");
            } else if (t != null) {
                r.setContentName(t.getSeriesName());
                r.setContentType("Show");
            }

            latestReviews.add(r);
        }

        int displayNum = Integer.parseInt(env.getProperty("num_search_results"));
        if(latestReviews.size() > displayNum ){
            latestReviews = latestReviews.subList(0, displayNum);
        }

        model.addAttribute("latestReviews", latestReviews);

        List<Movie> topBoxOffice;
        topBoxOffice = movieService.getAllMovies();
        for (Movie m : topBoxOffice) {
            m.calculateOverallRating();
            m.calculateBoxOffice();
        }
        Collections.sort(topBoxOffice, new Comparator<Movie>() {
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

        Collections.reverse(topBoxOffice);
        if(topBoxOffice.size() > displayNum) {
            topBoxOffice = topBoxOffice.subList(0, displayNum);
        }

        model.addAttribute("posterURL", movieService.getMoviePosterURL());
        model.addAttribute("topBoxOffice", topBoxOffice);
        DecimalFormat df = new DecimalFormat("#.##");
        model.addAttribute("decimalFormat", df);

        return "index";
    }
}
