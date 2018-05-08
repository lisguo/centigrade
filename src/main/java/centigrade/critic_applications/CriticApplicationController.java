package centigrade.critic_applications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CriticApplicationController {
    @Autowired
    private CriticApplicationService applicationService;

    @GetMapping("/critic_application")
    public String displayCriticApplication(){
        return "critic_application";
    }

    @PostMapping("/submit_critic_application")
    public String submitCriticApplication(@RequestParam long id, @RequestParam String sourceName,
                                          @RequestParam String sourceUrl, @RequestParam String resumeText, Model model){
        if(applicationService.exists(id)){
            model.addAttribute("notificationTitle","Error!");
            model.addAttribute("notificationDetails", "You have already submitted an application!");
        }
        else {
            CriticApplication app = applicationService.submitCriticApplication(id, sourceName, sourceUrl, resumeText);
            if (app != null) {
                model.addAttribute("notificationTitle", "Success!");
                model.addAttribute("notificationDetails", "We will review your application and will " +
                        "contact you if you have been approved.");
            } else {
                model.addAttribute("notificationTitle", "Error!");
                model.addAttribute("notificationDetails", "Please try again later.");
            }
        }
        return "notification_modal";
    }
}
