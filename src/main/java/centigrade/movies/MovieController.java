package centigrade.movies;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MovieController {

    @RequestMapping("/movie")
    public String movie(Model model) {
        // Adding dummy movie info
        String title = "Wall-E";
        String summary = "After hundreds of lonely years of doing what he was " +
                "built for, WALL-E (short for Waste Allocation Load Lifter Earth-Class) discovers a new purpose in life " +
                "(besides collecting knick-knacks) when he meets a sleek search robot named EVE.";

        // Create movie with info and add to model
        Movie movie = new Movie(title, summary);
        model.addAttribute("movie", movie);

        return "movie";
    }

}
