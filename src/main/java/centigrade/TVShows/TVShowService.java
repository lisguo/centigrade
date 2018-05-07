package centigrade.TVShows;

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
import java.util.Calendar;
import java.util.List;

@Service
public class TVShowService {
    @Autowired
    private TVShowRepository tvShowRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private Environment env;

    @Autowired
    JdbcTemplate template;

    public List<TVShow> getAllTVShows() {
        return tvShowRepository.findAll();
    }

    public List<TVShow> getAllTVShowsSortedBySeriesName() {
        return tvShowRepository.findAllByOrderBySeriesName();
    }

    public TVShow getTVShowById(long id) {
        return tvShowRepository.findTVShowById(id);
    }

    public Episode getEpisodeById(long id) {
        return episodeRepository.findEpisodeById(id);
    }

    public String getTVShowPosterURL() {
        String path = env.getProperty("tv_poster_dir");
        return path;
    }

    public List<TVShow> getTVography(Person p) {
        return template.query("SELECT contentId, castId FROM casttocontent t1 " +
                "INNER JOIN tvshows t2 ON t1.contentId = t2.id WHERE castId='" +
                p.getId() + "'", new ResultSetExtractor<List<TVShow>>() {
            @Override
            public List<TVShow> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<TVShow> shows = new ArrayList<TVShow>();

                while (rs.next()) {
                    TVShow t = tvShowRepository.findTVShowById(rs.getLong(1));
                    if (t != null) {
                        shows.add(t);
                    }
                }

                return shows;
            }
        });
    }

    public List<TVShow> getLikeShows(String token) {
        if (token.length() <= Integer.parseInt(env.getProperty("small_word_threshold"))) {
            token = " " + token + " ";
        }
        return template.query("SELECT id FROM tvshows WHERE seriesname LIKE \"%" +
                token + "%\"OR soundex(seriesname) LIKE soundex(\"" + token + "\")", new ResultSetExtractor<List<TVShow>>() {
            @Override
            public List<TVShow> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<TVShow> shows = new ArrayList<TVShow>();

                while (rs.next()) {
                    TVShow t = tvShowRepository.findTVShowById(rs.getLong(1));
                    if (t != null) {
                        shows.add(t);
                    }
                }

                return shows;
            }
        });
    }
    public String[] getNextDays(int N){
        String [] next = new String[N];
        Calendar now = Calendar.getInstance();
        for(int i = 0;i < N;i++){
            next[i] = now.get(Calendar.YEAR)+"-"+ now.get(Calendar.MONTH)+"-"+now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.DATE,1);
        }
        return next;

    }

    public List<TVShow> getLatestMovies(int days) {
        String [] nextDays = getNextDays(days);
        ArrayList<TVShow> tvShows = new ArrayList<TVShow>();
        for (int i =0;i<nextDays.length;i++){
            tvShows.addAll(getEpisodeByDate(nextDays[i]));

        }
        return tvShows;

    }
    public List<TVShow> getEpisodeByDate(String date) {

        return template.query("select  T.id from tvshows T, episodes E where E.firstaired = '"+date+";", new ResultSetExtractor<List<TVShow>>() {
            @Override
            public List<TVShow> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<TVShow> shows = new ArrayList<TVShow>();

                while (rs.next()) {
                    TVShow t = tvShowRepository.findTVShowById(rs.getLong(1));
                    if (t != null) {
                        shows.add(t);
                    }
                }

                return shows;
            }
        });
    }

    public List<Episode> getSeason(TVShow show, int season) {
        return episodeRepository.findEpisodesBySeriesIdAndSeasonNumber(show.getId(), season);
    }

    public void saveShow(TVShow t) {
        tvShowRepository.save(t);
    }

}