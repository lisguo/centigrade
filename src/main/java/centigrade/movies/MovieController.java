package centigrade.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/add_movie")
    public String addMovieForm(){
        return "add_movie";
    }

    @PostMapping("/add_movie")
    public @ResponseBody String addMovieSubmit (@RequestParam String title, @RequestParam String plot) {
        movieService.addMovie(title, plot);
        return "Saved";
    }

    @GetMapping("/movies")
    public String displayAllMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies"; // Show movie.html in templates
    }

    @GetMapping("/movie")
    public String displayMovie (@RequestParam long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("posterURL", movieService.getMoviePosterURL(movie));
        return "movie";
    }

}
