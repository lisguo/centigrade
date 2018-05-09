package centigrade.critic_applications;

import centigrade.accounts.Account;
import centigrade.accounts.AccountService;
import centigrade.accounts.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class CriticApplicationController {
    @Autowired
    Environment env;

    @Autowired
    private CriticApplicationService applicationService;

    @GetMapping("/critic_application_form")
    public String displayCriticApplication(){
        return "critic_application_form";
    }

    @PostMapping("/submit_critic_application")
    public String submitCriticApplication(@RequestParam long id, @RequestParam String sourceName,
                                          @RequestParam String sourceUrl, @RequestParam String resumeText, Model model){
        String duplMsg = env.getProperty("critic_app_duplicate");
        String successMsg = env.getProperty("critic_app_success");
        String errorMsg = env.getProperty("error_message");

        if(applicationService.exists(id)){
            model.addAttribute("notificationTitle","Error!");
            model.addAttribute("notificationDetails", duplMsg);
        }
        else {
            CriticApplication app = applicationService.submitCriticApplication(id, sourceName, sourceUrl, resumeText);
            if (app != null) {
                model.addAttribute("notificationTitle", "Success!");
                model.addAttribute("notificationDetails", successMsg);
            } else {
                model.addAttribute("notificationTitle", "Error!");
                model.addAttribute("notificationDetails", errorMsg);
            }
        }
        return "notification_alert";
    }

    @GetMapping("/get_critic_applications")
    public String getAllCriticApplications(HttpSession session, Model model){
        Account a = (Account) session.getAttribute("account");
        if (a == null || a.getAccountType() != AccountType.ADMIN) {
            return "redirect:/";
        }

        Iterable<CriticApplication> applications = applicationService.getAllApplications();
        applicationService.setAccountInfo(applications);
        model.addAttribute("applications", applications);
        return "review_critic_applications";
    }
}
