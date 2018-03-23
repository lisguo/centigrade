package centigrade.movies;

import centigrade.people.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private Environment env;

    @Autowired
    JdbcTemplate template;

    public void addMovie(String title, String plot){
        Movie m = new Movie();
        m.setTitle(title);
        m.setPlot(plot);
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

    public List<Movie> getFilmography(Person p){
        return template.query("SELECT * FROM actor_in_movie WHERE actor_id='" + p.getId() +"'", new ResultSetExtractor<List<Movie>>() {
            @Override
            public List<Movie> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Movie> films = new ArrayList<Movie>();

                while(rs.next())
                {
                    Movie m = movieRepository.findMovieById(rs.getInt(2));
                    films.add(m);
                }

                return films;
            }
        });
    }
}
