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
import centigrade.reviews.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.env.Environment;

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
    public String displayAllTVShowsMapping(Model model) {
        return displayAllTVShows(model,1);

    }
    @GetMapping("/shows{pageNum}")
    public String displayAllTVShowsMappingWithPage(Model model, @PathVariable("pageNum") String pageNum) {
        return displayAllTVShows(model,Integer.parseInt(pageNum));

    }
    public String displayAllTVShows(Model model,int page) {
        List<TVShow> shows = tvShowService.getAllTVShows();
        for (TVShow t : shows) {
            t.calculateOverallRating();
        }
        List<TVShow> showsOut = new ArrayList<TVShow>();

        int searchAmount =Integer.parseInt(env.getProperty("num_search_results"));
        int end = page * searchAmount;
        int start = (page-1)*searchAmount;
        for(int i =start; i<end && i<showsOut.size();i++){
            showsOut.add(showsOut.get(i));
        }

        model.addAttribute("shows", showsOut);
        model.addAttribute("posterURL", tvShowService.getTVShowPosterURL());
        DecimalFormat df = new DecimalFormat("#.##");
        model.addAttribute("decimalFormat", df);
        return "shows";
    }


    @GetMapping("/show")
    public String displayTVShow(@RequestParam long id, @RequestParam(defaultValue = "1") int season, Model model) {
        TVShow show = tvShowService.getTVShowById(id);
        model.addAttribute("show", show);
        model.addAttribute("posterURL", tvShowService.getTVShowPosterURL());
        model.addAttribute("photoURL", personService.getPersonPhotoURL());

        List<Episode> selectedSeason = tvShowService.getSeason(show, season);
        model.addAttribute("season", selectedSeason);

        List<Person> cast = personService.getCastByTVShow(show);
        model.addAttribute("cast", cast);

        List<Review> reviews = reviewService.getReviewsByContent(id);
        ArrayList<Review> userReviews = new ArrayList<>();
        ArrayList<Review> criticReviews = new ArrayList<>();
        Account a;

        for (Review r : reviews) {

            if (r.getReviewText() == null) {
                continue;
            }

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