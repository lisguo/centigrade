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

    public String getMoviePosterURL(){
        String path = env.getProperty("movie_poster_dir");
        return path;
    }
    public String getMovieTrailerURL(){
        String path = env.getProperty("movie_trailer_dir");
        return path;
    }

    public List<Movie> getFilmography(Person p){
        return template.query("SELECT contentId, castId FROM casttocontent t1 INNER JOIN movies t2 ON t1.contentId = t2.id WHERE castId='" + p.getId() +"'", new ResultSetExtractor<List<Movie>>() {
            @Override
            public List<Movie> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Movie> films = new ArrayList<Movie>();

                while(rs.next())
                {
                    Movie m = movieRepository.findMovieById(rs.getInt(1));
                    films.add(m);
                }

                return films;
            }
        });
    }
    public List<Movie> getLikeMovies(String token){
        if(token.length()>Integer.parseInt(env.getProperty("small_word_threshold"))){
            token = ""+token+"";
        }
        return template.query("select id from movies where title like \"%"+token+"%\"or soundex(title) like soundex(\""+token+"\")", new ResultSetExtractor<List<Movie>>() {
//        return template.query("SELECT contentId FROM casttocontent t1 INNER JOIN movies t2 ON t1.contentId = t2.id WHERE castId='" + p.getId() +"'", new ResultSetExtractor<List<Movie>>() {
            @Override
            public List<Movie> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Movie> films = new ArrayList<Movie>();

                while(rs.next())
                {
                    Movie m = movieRepository.findMovieById(rs.getInt(1));
                    films.add(m);
                }

                return films;
            }
        });
    }

    public void saveMovie(Movie m){
        movieRepository.save(m);
    }
}
