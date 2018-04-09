package centigrade.reviews;

import centigrade.accounts.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ReviewController {

    @Autowired
    private Environment env;

    @Autowired
    private ReviewService reviewService;

    @PostMapping("add_review")
    public String addReview(@RequestParam String reviewtext, @RequestParam double rating,
                            @RequestParam String contentType, @RequestParam long contentID, HttpSession session){
        Account a = (Account) session.getAttribute("account");
        if(a == null){
            return "login";
        }

        if(reviewtext.trim().equals("Add Review (Optional)"))
        {
            reviewtext = null;
        }

        reviewService.addReview(contentID, a.getId(), rating, reviewtext);
        return "index";
    }
}
