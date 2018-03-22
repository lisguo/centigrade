package centigrade.people;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/person")
    public String displayPerson (@RequestParam long id, Model model) {
        Person person = personService.getPersonById(id);
        model.addAttribute("person", person);
        model.addAttribute("photoURL", personService.getPersonPhotoURL(person));
        return "person";
    }

}
