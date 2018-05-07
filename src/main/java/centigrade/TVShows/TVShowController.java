package centigrade.TVShows;

import centigrade.accounts.*;
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
import org.springframework.core.env.Environment;

enum TVShowSortCriteria {
    TITLE, RATING, FIRST_AIRED,POPULAR
}

enum TVShowSortDirection {
    ASCENDING, DESCENDING
}

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
    public String displayAllTVShows(Model model, @RequestParam(defaultValue = "TITLE") String sortBy,
                                    @RequestParam(defaultValue = "ASCENDING") String sortDirection,
                                    @RequestParam(defaultValue="1") int page) {

        List<TVShow> shows = tvShowService.getAllTVShows();

        if (sortBy.equals("TITLE")) {
            shows = tvShowService.getAllTVShowsSortedBySeriesName();
        } else {
            shows = tvShowService.getAllTVShows();
        }

        for (TVShow t : shows) {
            t.calculateOverallRating();
        }

        if (sortBy.equals("RATING")) {
            Collections.sort(shows, new Comparator<TVShow>() {
                @Override
                public int compare(TVShow t1, TVShow t2) {
                    if (t1.getOverallRating() > t2.getOverallRating()) {
                        return 1;
                    } else if (t1.getOverallRating() < t2.getOverallRating()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        } else if (sortBy.equals("FIRST_AIRED")) {
            Collections.sort(shows, new Comparator<TVShow>() {
                @Override
                public int compare(TVShow t1, TVShow t2) {
                    if (t1.getFirstAired() == null && t2.getFirstAired() == null) {
                        return 0;
                    } else if (t1.getFirstAired() == null) {
                        return -1;
                    } else if (t2.getFirstAired() == null) {
                        return 1;
                    }

                    String[] t1_split = t1.getFirstAired().split("-");
                    String[] t2_split = t2.getFirstAired().split("-");

                    int t1_year = Integer.parseInt(t1_split[0]);
                    int t1_month = Integer.parseInt(t1_split[1]);
                    int t1_day = Integer.parseInt(t1_split[2]);

                    int t2_year = Integer.parseInt(t2_split[0]);
                    int t2_month = Integer.parseInt(t2_split[1]);
                    int t2_day = Integer.parseInt(t2_split[2]);

                    if (t1_year > t2_year) {
                        return 1;
                    } else if (t1_year < t2_year) {
                        return -1;
                    } else {
                        if (t1_month > t2_month) {
                            return 1;
                        } else if (t1_month < t2_month) {
                            return -1;
                        } else {
                            if (t1_day > t2_day) {
                                return 1;
                            } else if (t1_day < t2_day) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    }
                }
            });
        }else if (sortBy.equals("POPULAR")) {
            Collections.sort(shows, new Comparator<TVShow>() {
                @Override
                public int compare(TVShow t1, TVShow t2) {

                    if (t1.getOverallRating() > t2.getOverallRating()) {
                        return 1;
                    } else if (t1.getOverallRating() < t2.getOverallRating()) {
                        return -1;
                    } else {
                        if (t1.getTimesRated() > t2.getTimesRated()) {
                            return 1;
                        } else if (t1.getTimesRated() < t2.getTimesRated()) {
                            return -1;
                        } else {
                            return 0;
                        }
//                        return 0;
                    }
                }
            });
        }

        if (sortDirection.equals("DESCENDING")) {
            Collections.reverse(shows);
        }
        List<TVShow> outShows = new ArrayList<TVShow>();
        int searchAmount = Integer.parseInt(env.getProperty("num_search_results"));
        int end = page * searchAmount;
        int start = (page - 1) * searchAmount;
        for (int i = start; i < end && i < shows.size(); i++) {
            outShows.add(shows.get(i));
        }

        model.addAttribute("sortCriteria", EnumSet.allOf(TVShowSortCriteria.class));
        model.addAttribute("sortDirections", EnumSet.allOf(TVShowSortDirection.class));
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("shows", outShows);
        String endLink = "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        if (page != 1) model.addAttribute("prev", "/shows?page=" + (page - 1) + endLink);
        if (end + 1 < shows.size()) model.addAttribute("next", "/shows?page=" + (page + 1) + endLink);
        model.addAttribute("posterURL", tvShowService.getTVShowPosterURL());
        DecimalFormat df = new DecimalFormat("#.##");
        model.addAttribute("decimalFormat", df);
        return "shows";
    }


    @GetMapping("/show")
    public String displayTVShow(@RequestParam long id, @RequestParam(required = false) ReviewResult res,
                                @RequestParam(required = false)WishListResult wishList,
                                @RequestParam(required = false)WishListResult notInterested,
                                @RequestParam(defaultValue = "1") int season, Model model) {
        TVShow show = tvShowService.getTVShowById(id);
        model.addAttribute("show", show);
        model.addAttribute("posterURL", tvShowService.getTVShowPosterURL());
        model.addAttribute("photoURL", personService.getPersonPhotoURL());

        if (res == ReviewResult.SUCCESS) {
            model.addAttribute("message", env.getProperty("review_success"));
        } else if (res == ReviewResult.ALREADY_REVIEWED) {
            model.addAttribute("message", env.getProperty("review_already_reviewed"));
        } else if (res == ReviewResult.DELETED) {
            model.addAttribute("message", env.getProperty("review_deleted"));
        } else if (res == ReviewResult.EDITED) {
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

            if (a == null) {
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