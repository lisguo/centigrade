package centigrade.people;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import centigrade.movies.Movie;
import centigrade.movies.MovieService;
import java.util.List;

@Controller
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private MovieService movieService;

    @GetMapping("/person")
    public String displayPerson (@RequestParam long id, Model model) {
        Person person = personService.getPersonById(id);
        model.addAttribute("person", person);
        model.addAttribute("photoURL", personService.getPersonPhotoURL());
        model.addAttribute("posterURL", movieService.getMoviePosterURL());

        List<Movie> films =  movieService.getFilmography(person);
        model.addAttribute("films", films);
        return "person";
    }

}
