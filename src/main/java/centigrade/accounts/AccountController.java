package centigrade.accounts;

import centigrade.TVShows.TVShow;
import centigrade.TVShows.TVShowService;
import centigrade.movies.Movie;
import centigrade.movies.MovieService;
import centigrade.reported_accounts.ReportedAccountService;
import centigrade.reviews.Review;
import centigrade.reviews.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.ByteArrayInputStream;

import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;





import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private ReportedAccountService reportedAccountService;

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

        Account a = accountService.getAccountByEmail(email);

        byte[] salt = a.getSalt();
        byte[] hashedPassword = accountService.hashPassword(password, salt);

        if (!validPassword(hashedPassword, a.getPassword())) {
            model.addAttribute("message", env.getProperty("login_error"));
            return "login";
        }

        if (a.getIsActive() != 1) {
            model.addAttribute("message", env.getProperty("login_unverified"));
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
            Movie m = movieService.getMovieById(r.getContentId());
            TVShow t = tvShowService.getTVShowById(r.getContentId());

            if (m != null) {
                r.setContentName(m.getTitle());
                movieReviews.add(r);
            } else if (t != null) {
                r.setContentName(t.getSeriesName());
                showReviews.add(r);
            }
        }

        model.addAttribute("movieReviews", movieReviews);
        model.addAttribute("showReviews", showReviews);

        model.addAttribute("photoURL", accountService.getUserPhotoURL());
        model.addAttribute("posterURL", movieService.getMoviePosterURL());
        model.addAttribute("tvPosterURL", tvShowService.getTVShowPosterURL());
        List<WishListItem> wishList = accountService.getWishList(a);
        model.addAttribute("wishList", wishList);
        List<WishListItem> notInterestedList = accountService.getNotInterestedList(a);
        model.addAttribute("notInterestedList", notInterestedList);

        return "profile";
    }

    @PostMapping("/add_to_wishlist")
    public RedirectView addToWishList(@RequestParam long contentID, HttpSession session){
        RedirectView rv = new RedirectView();

        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            rv.setUrl("login");
            return rv;
        }

        Movie m = movieService.getMovieById(contentID);
        TVShow t = tvShowService.getTVShowById(contentID);

        // is this already on the user's wishlist?
        for(WishListItem w : accountService.getWishList(a)){
            if(w.getId() == contentID){
                if(m != null){
                    rv.setUrl("movie?id=" + contentID + "&wishList=" + WishListResult.EXISTS);
                    return rv;
                }else if(t != null){
                    rv.setUrl("show?id=" + contentID + "&wishList=" + WishListResult.EXISTS);
                    return rv;
                }

            }
        }

        if(m != null){
            rv.setUrl("movie?id=" + contentID + "&wishList=" + WishListResult.ADDED);
            accountService.insertIntoWishList(a.getId(), contentID, true);
        }else if(t != null){
            rv.setUrl("show?id=" + contentID + "&wishList=" + WishListResult.ADDED);
            accountService.insertIntoWishList(a.getId(), contentID, false);
        }

        return rv;
    }

    @PostMapping("/add_to_not_interested_list")
    public RedirectView addToNotInterestedList(@RequestParam long contentID, HttpSession session){
        RedirectView rv = new RedirectView();

        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            rv.setUrl("login");
            return rv;
        }

        Movie m = movieService.getMovieById(contentID);
        TVShow t = tvShowService.getTVShowById(contentID);

        // is this already on the user's not interested list?
        for(WishListItem w : accountService.getNotInterestedList(a)){
            if(w.getId() == contentID){
                if(m != null){
                    rv.setUrl("movie?id=" + contentID + "&notInterested=" + WishListResult.EXISTS);
                    return rv;
                }else if(t != null){
                    rv.setUrl("show?id=" + contentID + "&notInterested=" + WishListResult.EXISTS);
                    return rv;
                }

            }
        }

        if(m != null){
            rv.setUrl("movie?id=" + contentID + "&notInterested=" + WishListResult.ADDED);
            accountService.insertIntoNotInterestedList(a.getId(), contentID, true);
        }else if(t != null){
            rv.setUrl("show?id=" + contentID + "&notInterested=" + WishListResult.ADDED);
            accountService.insertIntoNotInterestedList(a.getId(), contentID, false);
        }

        return rv;
    }

    @PostMapping("/remove_from_wishlist")
    public RedirectView removeFromWishList(@RequestParam long contentID, HttpSession session){
        RedirectView rv = new RedirectView();

        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            rv.setUrl("login");
            return rv;
        }

        accountService.removeFromWishList(a.getId(), contentID);

        rv.setUrl("profile?id=" + a.getId());
        return rv;
    }

    @PostMapping("/remove_from_not_interested_list")
    public RedirectView removeFromNotInterestedList(@RequestParam long contentID, HttpSession session){
        RedirectView rv = new RedirectView();

        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            rv.setUrl("login");
            return rv;
        }

        accountService.removeFromNotInterestedList(a.getId(), contentID);

        rv.setUrl("profile?id=" + a.getId());
        return rv;
    }

    @PostMapping("/delete_user")
    public String deleteUser(long userId, @RequestParam(required=false) boolean deleteReport, Model model){
        if(deleteReport){
            reportedAccountService.deleteAllReportedAccounts(userId);
        }

        List<Review> reviews = reviewService.getReviewsByUser(userId);
        for(Review r : reviews){
            reviewService.deleteReview(r);
        }

        accountService.deleteAccount(userId);

        String successMsg = env.getProperty("delete_user_success");
        model.addAttribute("notificationTitle", "Success!");
        model.addAttribute("notificationDetails", successMsg);

        return "notification_alert";
    }

    @PostMapping("/upload_photo")
    public RedirectView uploadPhoto(@RequestParam("file") MultipartFile file,
                                    HttpSession session){
        RedirectView rv = new RedirectView();

        Account a = (Account) session.getAttribute("account");
        if (a == null) {
            rv.setUrl("login");
            return rv;
        }

        if(!file.isEmpty()) {
            FTPClient con = null;

            con = new FTPClient();
            try {
                byte[] bytes = file.getBytes();
                System.out.println(bytes.length);
                if(bytes.length>1048574){
                    //Filesize is too large
                    rv.setUrl("profile?id=" + a.getId());
                    return rv;

                }

                    con.connect("130.245.155.104");

                    if (con.login("simrit", "CS69AAux"))
                    {
//                        System.out.println("Login");
                        con.enterLocalPassiveMode();

                        con.setFileType(FTP.BINARY_FILE_TYPE);
                        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                        boolean result = con.storeFile("users/user"+a.getId()+".jpg", in);
                        in.close();
                        con.logout();
                        con.disconnect();
                    }
                    
            } catch (IOException e) {

                rv.setUrl("profile?id=" + a.getId());
                return rv;
            }finally {
                if(con.isConnected()) {
                    try {
                        con.disconnect();
                    } catch(IOException ioe) {
                        // do nothing
                    }
                }

            }
        }

        rv.setUrl("profile?id=" + a.getId());
        return rv;
    }

    @GetMapping("/critics")
    public String critics(Model model){
        List<Account> accounts = accountService.getAllAccounts();
        List<Account> critics = new ArrayList<>();
        for(Account a : accounts){
            if(a.getAccountType() == AccountType.CRITIC){
                List<Review> reviews = reviewService.getReviewsByUser(a.getId());
                if(reviews != null) {
                    a.setNumReviews(reviews.size());
                }else{
                    a.setNumReviews(0);
                }
                critics.add(a);
            }
        }

        Collections.sort(critics, new Comparator<Account>() {
            @Override
            public int compare(Account a1, Account a2) {
                if (a1.getNumReviews() > a2.getNumReviews()) {
                    return 1;
                } else if (a1.getNumReviews() < a2.getNumReviews()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        Collections.reverse(critics);

        model.addAttribute("photoURL", env.getProperty("user_photo_dir"));
        model.addAttribute("critics", critics);
        return "critics";
    }
}
