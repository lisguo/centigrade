package centigrade;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

    @Value("${app_name}")
    private String appName;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("appName", this.appName);
        return "index";
    }

    @RequestMapping("/add_movie")
    public String addMovie(){
        return "add_movie";
    }
}
