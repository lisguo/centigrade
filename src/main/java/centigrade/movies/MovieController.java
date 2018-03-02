package centigrade.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @RequestMapping("/addMovie")
    public @ResponseBody String addNewMovie (@RequestParam String title, @RequestParam String summary) {

        Movie m = new Movie();
        m.setTitle(title);
        m.setSummary(summary);
        movieRepository.save(m);
        return "Saved";
    }

    @RequestMapping("/movies")
    public String getAllMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAll()); // Get all movies in database
        return "movies"; // Show movie.html in templates
    }

    @RequestMapping("/movie")
    public String displayMovie (@RequestParam long id, Model model) {
        model.addAttribute("movie", movieRepository.findOne(id));
        return "movie";
    }

}
