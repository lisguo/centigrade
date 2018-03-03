package centigrade.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private Environment env;

    public void addMovie(String title, String summary){
        Movie m = new Movie();
        m.setTitle(title);
        m.setSummary(summary);
        movieRepository.save(m);
    }

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie getMovieById(long id){
        return movieRepository.findMovieById(id);
    }

    public String getMoviePosterURL(Movie m){
        String title =  m.getTitle();
        title = title.replace(":", "_");
        String path = env.getProperty("movie_poster_dir") + title + ".jpg";
        return path;
    }
}
