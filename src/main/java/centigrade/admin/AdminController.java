package centigrade.admin;

import centigrade.accounts.Account;
import centigrade.accounts.AccountType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AdminController {

    @GetMapping("admin")
    public String showAdminPage(HttpSession session){
        Account user = (Account) session.getAttribute("account");
        if(user.getAccountType() != AccountType.ADMIN){
            return "redirect:/";
        }
        return "admin";
    }
}
