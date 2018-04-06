package centigrade.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private Environment env;

    @GetMapping("register")
    public String registerForm(){
        return "register";
    }

    @PostMapping("register")
    public String registerSubmit(@RequestParam String email, @RequestParam String password, @RequestParam String passwordVerify,
                                 @RequestParam String firstName, @RequestParam String lastName, Model model){
        if(!accountService.isValidRegister(email)){
            model.addAttribute("alert", "invalidEmail");
            model.addAttribute("message", env.getProperty("register_email_error"));
            return "register";
        }
        accountService.addAccount(email,password,firstName,lastName);
        model.addAttribute("alert", "success");
        model.addAttribute("message", env.getProperty("register_email_success"));
        return "register";
    }

    @GetMapping("login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password, Model model, HttpSession session){
        if(!accountService.isValidLogin(email)){
            model.addAttribute("message", env.getProperty("login_error"));
            return "login";
        }

        // Check password
        Account a = accountService.getAccountByEmail(email);
        byte[] salt = a.getSalt();
        byte[] hashedPassword = accountService.hashPassword(password, salt);

        if(!validPassword(hashedPassword, a.getPassword())){
            model.addAttribute("message", env.getProperty("login_error"));
            return "login";
        }
        session.setAttribute("account", a);
        model.addAttribute("appName", env.getProperty("app_name"));
        return "index";
    }
    @PostMapping("logout")
    public String loginSubmit(Model model, HttpSession session){

        session.setAttribute("account", null);
        model.addAttribute("appName", env.getProperty("app_name"));
        return "index";
    }
    @GetMapping("logout")
    public String loginSubmit2(Model model, HttpSession session){

        session.setAttribute("account", null);
        model.addAttribute("appName", env.getProperty("app_name"));
        return "index";
    }

    private boolean validPassword(byte[] password1, byte[] password2){
        if(password1.length != password2.length){
            return false;
        }
        for(int i = 0; i < password1.length; i++){
            if(password1[i] != password2[i]){
                return false;
            }
        }
        return true;
    }

    @GetMapping("/account")
    public String displayAccount (@RequestParam long id, Model model) {
        Account a = accountService.getAccountById(id);
        model.addAttribute("account", a);

        return "account";
    }

}
