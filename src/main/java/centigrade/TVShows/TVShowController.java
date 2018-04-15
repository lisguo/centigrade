package centigrade.TVShows;

import centigrade.accounts.Account;
import centigrade.accounts.AccountService;
import centigrade.accounts.AccountType;
import centigrade.people.PersonService;
import centigrade.people.Person;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import centigrade.reviews.Review;
import centigrade.reviews.ReviewResult;
import centigrade.reviews.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TVShowController {

    @Autowired
    private TVShowService tvShowService;
    @Autowired
    private PersonService personService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private Environment env;

    @GetMapping("/shows")
    public String displayAllTVShows(Model model) {
        List<TVShow> shows = tvShowService.getAllTVShows();
        for (TVShow t : shows) {
            t.calculateOverallRating();
        }
        model.addAttribute("shows", shows);
        model.addAttribute("posterURL", tvShowService.getTVShowPosterURL());
        DecimalFormat df = new DecimalFormat("#.##");
        model.addAttribute("decimalFormat", df);
        return "shows";
    }

    @GetMapping("/show")
    public String displayTVShow(@RequestParam long id, @RequestParam(required = false) ReviewResult res,
                                @RequestParam(defaultValue = "1") int season, Model model) {
        TVShow show = tvShowService.getTVShowById(id);
        model.addAttribute("show", show);
        model.addAttribute("posterURL", tvShowService.getTVShowPosterURL());
        model.addAttribute("photoURL", personService.getPersonPhotoURL());

        if(res == ReviewResult.SUCCESS) {
            model.addAttribute("message", env.getProperty("review_success"));
        }
        else if(res == ReviewResult.ALREADY_REVIEWED){
            model.addAttribute("message", env.getProperty("review_already_reviewed"));
        }

        List<Episode> selectedSeason = tvShowService.getSeason(show, season);
        model.addAttribute("season", selectedSeason);

        List<Person> cast = personService.getCastByTVShow(show);
        model.addAttribute("cast", cast);

        List<Review> reviews = reviewService.getReviewsByContent(id);
        ArrayList<Review> userReviews = new ArrayList<>();
        ArrayList<Review> criticReviews = new ArrayList<>();
        Account a;

        for (Review r : reviews) {
            a = accountService.getAccountById(r.getUserId());

            if(a == null){
                continue;
            }

            r.setUserName(a.getFirstName() + " " + a.getLastName());

            if (a.getAccountType() == AccountType.CRITIC) {
                criticReviews.add(r);
            } else {
                userReviews.add(r);
            }
        }

        show.calculateOverallRating();

        if (show.getTimesRated() == 0) {
            model.addAttribute("rating", "Not Yet Rated");
        } else {
            model.addAttribute("rating", String.format("%.2f", show.getOverallRating()) + "%");
        }

        model.addAttribute("reviewsCounted", show.getTimesRated());
        model.addAttribute("userReviews", userReviews);
        model.addAttribute("criticReviews", criticReviews);

        return "show";
    }

    @GetMapping("/episode")
    public String displayEpisode(@RequestParam long id, Model model) {
        Episode episode = tvShowService.getEpisodeById(id);
        TVShow show = tvShowService.getTVShowById(episode.getSeriesId());
        model.addAttribute("show", show);
        model.addAttribute("episode", episode);
        model.addAttribute("posterURL", tvShowService.getTVShowPosterURL());
        model.addAttribute("photoURL", personService.getPersonPhotoURL());

        return "episode";
    }

}