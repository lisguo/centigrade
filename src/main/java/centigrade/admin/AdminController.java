package centigrade.admin;

import centigrade.Genre;
import centigrade.TVShows.DuplicateShowException;
import centigrade.TVShows.TVShow;
import centigrade.TVShows.TVShowService;
import centigrade.accounts.Account;
import centigrade.accounts.AccountService;
import centigrade.accounts.AccountType;
import centigrade.critic_applications.CriticApplication;
import centigrade.critic_applications.CriticApplicationService;
import centigrade.movies.DuplicateMovieException;
import centigrade.movies.Movie;
import centigrade.movies.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private Environment env;

    @Autowired
    private MovieService movieService;
    @Autowired
    private TVShowService showService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CriticApplicationService applicationService;

    @Autowired
    JdbcTemplate template;

    @GetMapping("admin")
    public String showAdminPage(HttpSession session, Model model){
        Account user = (Account) session.getAttribute("account");
        if(user == null || user.getAccountType() != AccountType.ADMIN){
            return "redirect:/login";
        }
        List<Genre> genreList = Arrays.asList(Genre.values());
        model.addAttribute("genreList", genreList);
        return "admin";
    }

    @PostMapping("create_show")
    public String createShow(@RequestParam String seriesName,
                              @RequestParam String contentRating, @RequestParam String firstAired,
                              @RequestParam String genres, @RequestParam String language,
                              @RequestParam String network, @RequestParam String overview,
                              @RequestParam String runtime, @RequestParam String status,
                              @RequestParam(value="posterImage", required=false) MultipartFile posterImage,
                              Model model){
        String successMsg = env.getProperty("create_show_success");
        String duplicateMsg = env.getProperty("create_show_duplicate_error");
        try {
            // MAKE ALL NEW SHOWS ONLY HAVE 1 SEASON. TODO: ADD SEASONS IN FORM
            TVShow show = showService.addShow(seriesName, contentRating, firstAired, 1, genres, language, network, overview, runtime, status);
            showService.uploadShowPoster(show, posterImage);
            model.addAttribute("notificationTitle", "Success!");
            model.addAttribute("notificationDetails", successMsg);
        } catch (DuplicateShowException e){
            model.addAttribute("notificationTitle", "Error!");
            model.addAttribute("notificationDetails", duplicateMsg);
        }
        return "notification_alert";
    }

    @PostMapping("create_movie")
    public String createMovie(@RequestParam String title, @RequestParam String year,
                              @RequestParam String rated, @RequestParam String released,
                              @RequestParam String runtime, @RequestParam String genre,
                              @RequestParam String plot, @RequestParam String boxoffice,
                              @RequestParam String production, @RequestParam String website,
                              @RequestParam(value="posterImage", required=false) MultipartFile posterImage,
                              Model model){
        String successMsg = env.getProperty("create_movie_success");
        String duplicateMsg = env.getProperty("create_movie_duplicate_error");
        boxoffice = "$" + boxoffice;
        int intYear = Integer.parseInt(year);
        try {
            Movie m = movieService.addMovie(title, intYear, rated, released, runtime, genre, plot, boxoffice, production, website);
            movieService.uploadMoviePoster(m, posterImage);
            model.addAttribute("notificationTitle", "Success!");
            model.addAttribute("notificationDetails", successMsg);
        } catch (DuplicateMovieException e){
            model.addAttribute("notificationTitle", "Error!");
            model.addAttribute("notificationDetails", duplicateMsg);
        }
        return "notification_alert";
    }

    @GetMapping("critic_application")
    public String applicationDetail(@RequestParam long id, Model model){
        CriticApplication application = applicationService.getApplicationById(id);
        applicationService.setAccountInfo(application);
        model.addAttribute("app", application);
        return "critic_application_detail";
    }

    @PostMapping("/accept_critic_application")
    public String acceptCriticApplication(@RequestParam long id, Model model){
        long accountId = applicationService.getApplicationById(id).getAccount();
        accountService.upgradeToCritic(accountId);
        applicationService.deleteApplication(id);

        String acceptMsg = env.getProperty("critic_application_accept");
        model.addAttribute("notificationTitle", "Accepted!");
        model.addAttribute("notificationDetails", acceptMsg);
        return "notification_alert";
    }

    @PostMapping("/reject_critic_application")
    public String rejectCriticApplication(@RequestParam long id, Model model){
        applicationService.deleteApplication(id);

        String acceptMsg = env.getProperty("critic_application_reject");
        model.addAttribute("notificationTitle", "Rejected!");
        model.addAttribute("notificationDetails", acceptMsg);
        return "notification_alert";
    }
}
