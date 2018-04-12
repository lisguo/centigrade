package centigrade.accounts;

import centigrade.TVShows.TVShow;
import centigrade.TVShows.TVShowService;
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private TVShowService tvShowService;

    @Autowired
    private Environment env;

    @GetMapping("register")
    public String registerForm(HttpSession session) {
        Account a = (Account) session.getAttribute("account");
        if (a != null) {
            return "index";
        }
        return "register";
    }

    @PostMapping("register")
    public String registerSubmit(@RequestParam String email, @RequestParam String password, @RequestParam String passwordVerify,
                                 @RequestParam String firstName, @RequestParam String lastName, Model model) {
        if (!accountService.isValidRegister(email)) {
            model.addAttribute("alert", "error");
            model.addAttribute("message", env.getProperty("register_email_error"));
            return "register";
        }
        Account a = accountService.addAccount(email, password, firstName, lastName);
        accountService.sendVerificationEmail(a, env.getProperty("verify_email_link"),
                env.getProperty("verify_email_subject"));

        model.addAttribute("alert", "success");
        model.addAttribute("message", env.getProperty("register_email_success"));
        return "register";
    }

    @GetMapping("edit_account")
    public String editAccount(HttpSession session) {
        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            return "login";
        }
        return "edit_account";
    }

    @PostMapping("edit_account")
    public String editAccount(@RequestParam String email, @RequestParam String oldPassword, @RequestParam String password, @RequestParam String passwordVerify,
                              @RequestParam String firstName, @RequestParam String lastName, Model model, HttpSession session) {
        Account a = (Account) session.getAttribute("account");
        if (email.length() > 0 && !a.getEmail().equals(email)) {
            if (!accountService.isValidRegister(email)) {
                model.addAttribute("alert", "invalidEmail");
                model.addAttribute("message", env.getProperty("register_email_error"));
                return "edit_account";
            }
        } else {
            email = null;
        }
        if (password.length() != 0) {
            if (!checkPassword(a, oldPassword)) {
                model.addAttribute("message", env.getProperty("login_error"));
                return "edit_account";
            } else {
                model.addAttribute("message", env.getProperty("register_email_success"));
            }

        } else {
            password = null;
        }
        if (firstName.length() == 0) {
            firstName = null;
        }
        if (lastName.length() == 0) {
            lastName = null;
        }
        accountService.updateAccount(a, email, password, firstName, lastName);
        model.addAttribute("alert", "success");

        return "edit_account";
    }

    @GetMapping("login")
    public String loginForm(HttpSession session) {
        Account a = (Account) session.getAttribute("account");
        if (a != null) {
            return "index";
        }
        return "login";
    }

    @PostMapping("login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        if (!accountService.isValidLogin(email)) {
            model.addAttribute("message", env.getProperty("login_error"));
            return "login";
        }

        // Check password
        Account a = accountService.getAccountByEmail(email);
        byte[] salt = a.getSalt();
        byte[] hashedPassword = accountService.hashPassword(password, salt);

        if (!validPassword(hashedPassword, a.getPassword())) {
            model.addAttribute("message", env.getProperty("login_error"));
            return "login";
        }
        session.setAttribute("account", a);
        model.addAttribute("appName", env.getProperty("app_name"));
        return "index";
    }

    @GetMapping("logout")
    public String logout(Model model, HttpSession session) {

        session.setAttribute("account", null);
        model.addAttribute("appName", env.getProperty("app_name"));
        return "redirect:/";
    }

    public boolean checkPassword(Account a, String password) {
        byte[] salt = a.getSalt();
        byte[] hashedPassword = accountService.hashPassword(password, salt);

        if (validPassword(hashedPassword, a.getPassword())) {
            return true;
        }
        return false;
    }

    private boolean validPassword(byte[] password1, byte[] password2) {
        if (password1.length != password2.length) {
            return false;
        }
        for (int i = 0; i < password1.length; i++) {
            if (password1[i] != password2[i]) {
                return false;
            }
        }
        return true;
    }

    @GetMapping("verify")
    public String verifyAccount(@RequestParam String nonce, Model model, HttpSession session) {
        Account a = accountService.verifyAccount(nonce);
        if (a != null) {
            model.addAttribute("alert", "success");
            model.addAttribute("message", env.getProperty("verify_success"));
            session.setAttribute("account", a);
        } else {
            model.addAttribute("alert", "error");
            model.addAttribute("message", env.getProperty("verify_error"));
        }
        return "index";
    }

    @GetMapping("/account")
    public String displayAccount(@RequestParam long id, Model model) {
        Account a = accountService.getAccountById(id);
        model.addAttribute("account", a);

        return "account";
    }

    @GetMapping("/profile")
    public String displayProfile(@RequestParam long id, Model model) {
        Account a = accountService.getAccountById(id);
        model.addAttribute("user", a);

        List<Review> reviews = reviewService.getReviewsByUser(a.getId());
        ArrayList<Review> movieReviews = new ArrayList<>();
        ArrayList<Review> showReviews = new ArrayList<>();

        for (Review r : reviews) {
            if (r.getReviewText() == null) {
                continue;
            }

            Movie m = movieService.getMovieById(r.getContentId());
            TVShow t = tvShowService.getTVShowById(r.getContentId());

            if (m != null) {
                r.setContentName(m.getTitle());
                movieReviews.add(r);
            }else if(t != null){
                r.setContentName(t.getSeriesName());
                showReviews.add(r);
            }
        }

        model.addAttribute("movieReviews", movieReviews);
        model.addAttribute("showReviews", showReviews);

        return "profile";
    }

}
