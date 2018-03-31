package centigrade.TVShows;

import centigrade.people.PersonService;
import centigrade.people.Person;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TVShowController {

    @Autowired
    private TVShowService tvShowService;
    @Autowired
    private PersonService personService;

    @GetMapping("/shows")
    public String displayAllTVShows(Model model) {
        model.addAttribute("shows", tvShowService.getAllTVShows());
        return "shows";
    }

    @GetMapping("/show")
    public String displayTVShow (@RequestParam long id, @RequestParam(defaultValue = "1") int season, Model model) {
        TVShow show = tvShowService.getTVShowById(id);
        model.addAttribute("show", show);
        model.addAttribute("posterURL", tvShowService.getTVShowPosterURL());
        model.addAttribute("photoURL", personService.getPersonPhotoURL());

        List<Episode> selectedSeason = tvShowService.getSeason(show, season);
        model.addAttribute("season", selectedSeason);

        List<Person> cast = personService.getCastByTVShow(show);
        model.addAttribute("cast", cast);

        return "show";
    }

    @GetMapping("/episode")
    public String displayEpisode (@RequestParam long id, Model model) {
        Episode episode = tvShowService.getEpisodeById(id);
        TVShow show = tvShowService.getTVShowById(episode.getSeriesId());
        model.addAttribute("show", show);
        model.addAttribute("episode", episode);
        model.addAttribute("posterURL", tvShowService.getTVShowPosterURL());
        model.addAttribute("photoURL", personService.getPersonPhotoURL());

        //List<Person> cast = personService.getCastByTVShow(show);
        //model.addAttribute("cast", cast);

        return "episode";
    }

}