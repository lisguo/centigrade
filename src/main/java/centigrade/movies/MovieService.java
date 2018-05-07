package centigrade.movies;

import centigrade.people.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import java.util.Calendar;
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

    public void addMovie(String title, int year, String rated, String released,
                         String runtime, String genre, String plot, String boxoffice,
                         String production, String website) {
        Movie m = new Movie();
        m.setTitle(title);
        m.setYear(year);
        m.setRated(rated);
        m.setReleased(released);
        m.setRuntime(runtime);
        m.setGenre(genre);
        m.setPlot(plot);
        m.setBoxoffice(boxoffice);
        m.setProduction(production);
        m.setWebsite(website);
        movieRepository.save(m);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getAllMoviesSortedByTitle() {
        return movieRepository.findAllByOrderByTitle();
    }

    public List<Movie> getAllMoviesSortedByYear() {
        return movieRepository.findAllByOrderByYear();
    }

    public Movie getMovieById(long id) {
        return movieRepository.findMovieById(id);
    }

    public String getMoviePosterURL() {
        String path = env.getProperty("movie_poster_dir");
        return path;
    }

    public String getMovieTrailerURL() {
        String path = env.getProperty("movie_trailer_dir");
        return path;
    }

    public List<Movie> getFilmography(Person p) {
        return template.query("SELECT contentId, castId FROM casttocontent t1 INNER JOIN movies t2 " +
                "ON t1.contentId = t2.id WHERE castId='" + p.getId() + "'", new ResultSetExtractor<List<Movie>>() {
            @Override
            public List<Movie> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Movie> films = new ArrayList<Movie>();

                while (rs.next()) {
                    Movie m = movieRepository.findMovieById(rs.getLong(1));
                    if (m != null) {
                        films.add(m);
                    }
                }
                return films;
            }
        });
    }

    public List<Movie> getLikeMovies(String token) {
        if (token.length() > Integer.parseInt(env.getProperty("small_word_threshold"))) {
            token = "" + token + "";
        }
        return template.query("SELECT id FROM movies WHERE title LIKE \"%" + token +
                "%\"OR soundex(title) LIKE soundex(\"" + token + "\")", new ResultSetExtractor<List<Movie>>() {
            @Override
            public List<Movie> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Movie> films = new ArrayList<Movie>();

                while (rs.next()) {
                    Movie m = movieRepository.findMovieById(rs.getLong(1));
                    if (m != null) {
                        films.add(m);
                    }
                }
                return films;
            }
        });
    }
    public String monthToMonth(int mon){

        String monthString;
        switch (mon) {
            case 1:  monthString = "Jan";
                break;
            case 2:  monthString = "Feb";
                break;
            case 3:  monthString = "Mar";
                break;
            case 4:  monthString = "Apr";
                break;
            case 5:  monthString = "May";
                break;
            case 6:  monthString = "Jun";
                break;
            case 7:  monthString = "Jul";
                break;
            case 8:  monthString = "Aug";
                break;
            case 9:  monthString = "Sep";
                break;
            case 10: monthString = "Oct";
                break;
            case 11: monthString = "Nov";
                break;
            default: monthString = "Dec";
                break;
        }
        return monthString;
    }
    public String[] getNextDays(int N){
        String [] next = new String[N];
        Calendar now = Calendar.getInstance();
        for(int i = 0;i < N;i++){
            String year = now.get(Calendar.YEAR)+"";
            String month = monthToMonth(now.get(Calendar.MONTH));
            String day = now.get(Calendar.DAY_OF_MONTH)+"";
            next[i] = day+" "+month+" "+year;
            now.add(Calendar.DATE,1);
        }
        return next;

    }
    public List<Movie> getLatestMovies(int days) {
        String [] nextDays = getNextDays(days);
        ArrayList<Movie> movies = new ArrayList<Movie>();
        for (int i =0;i<nextDays.length;i++){
            movies.addAll(getMovieDate(nextDays[i]));

        }
        return movies;

    }
    public List<Movie> getMovieDate(String date) {
        return template.query("select id from movies where released = '"+date+"';", new ResultSetExtractor<List<Movie>>() {
            @Override
            public List<Movie> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Movie> films = new ArrayList<Movie>();

                while (rs.next()) {
                    Movie m = movieRepository.findMovieById(rs.getLong(1));
                    if (m != null) {
                        films.add(m);
                    }
                }
                return films;
            }
        });
    }
    public void saveMovie(Movie m) {
        movieRepository.save(m);
    }
}
