package centigrade.movies;

import centigrade.ContentIdGenerator;
import centigrade.content.Content;
import centigrade.content.ContentRepository;
import centigrade.people.Person;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    private ContentRepository contentRepository;
    @Autowired
    private Environment env;
    @Autowired
    JdbcTemplate template;

    public Movie addMovie(String title, int year, String rated, String released,
                         String runtime, String genre, String plot, String boxoffice,
                         String production, String website) throws DuplicateMovieException {

        if (movieRepository.findMovieByTitle(title) != null){
            throw new DuplicateMovieException();
        }

        Content c = new Content();
        //c.setId(generatedId);
        contentRepository.save(c);

        Movie m = new Movie();
        m.setId(c.getId());
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

        return m;
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

    public List<Movie> getBestPictureWinners(){
        return template.query("SELECT winnerid FROM year_to_winner", new ResultSetExtractor<List<Movie>>() {
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
            String month = monthToMonth(now.get(Calendar.MONTH)+1);
            int day2 = now.get(Calendar.DAY_OF_MONTH);
            String day = day2+"";
            if (day2<10){
                day = "0"+day2;
            }
            next[i] = day+" "+month+" "+year;
            now.add(Calendar.DATE,1);
        }
        return next;

    }
    public List<Movie> getLatestMovies(int days) {
        String [] nextDays = getNextDays(days);
//        for(String i : nextDays){
//            System.out.println(i);
//        }
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
    public void deleteMovie(Movie m){ movieRepository.delete(m); }

    public boolean uploadMoviePoster(Movie m, MultipartFile file) {
        if (!file.isEmpty()) {
            FTPClient con = new FTPClient();
            try {
                byte[] bytes = file.getBytes();
                System.out.println(bytes.length);
                if (bytes.length > 1048574) {
                    return false;
                }

                con.connect("130.245.155.104");

                if (con.login("simrit", "CS69AAux")) {
                    con.enterLocalPassiveMode();

                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                    con.storeFile("posters/poster" + m.getId() + ".jpg", in);
                    in.close();
                    con.logout();
                    con.disconnect();

                    return true;
                }
            } catch (IOException e) {
                return false;
            } finally {
                if (con.isConnected()) {
                    try {
                        con.disconnect();
                    } catch (IOException ioe) {
                        // do nothing
                    }
                }
            }
        }
        return false;
    }
}
