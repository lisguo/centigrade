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

    public List<TVShow> getAllTVShows(){
        return tvShowRepository.findAll();
    }

    public TVShow getTVShowById(long id){
        return tvShowRepository.findTVShowById(id);
    }

    public Episode getEpisodeById(long id){return episodeRepository.findEpisodeById(id);}

    public String getTVShowPosterURL(){
        String path = env.getProperty("tv_poster_dir");
        return path;
    }

    public List<TVShow> getTVography(Person p){
        return template.query("SELECT contentId, castId FROM casttocontent t1 INNER JOIN tvshows t2 ON t1.contentId = t2.id WHERE castId='" + p.getId() +"'", new ResultSetExtractor<List<TVShow>>() {
            @Override
            public List<TVShow> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<TVShow> shows = new ArrayList<TVShow>();

                while(rs.next())
                {
                    TVShow t = tvShowRepository.findTVShowById(rs.getInt(1));
                    shows.add(t);
                }

                return shows;
            }
        });
    }
    public List<TVShow> getLikeShows(String token){
        if(token.length()<=Integer.parseInt(env.getProperty("small_word_threshold"))){
            token = " "+token+" ";
        }
        return template.query("select id from tvshows where seriesname like \"%"+token+"%\"or soundex(seriesname) like soundex(\""+token+"\")", new ResultSetExtractor<List<TVShow>>() {
//      return template.query("SELECT contentId, castId FROM casttocontent t1 INNER JOIN tvshows t2 ON t1.contentId = t2.id WHERE castId='" + p.getId() +"'", new ResultSetExtractor<List<TVShow>>() {
            @Override
            public List<TVShow> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<TVShow> shows = new ArrayList<TVShow>();

                while(rs.next())
                {
                    TVShow t = tvShowRepository.findTVShowById(rs.getInt(1));
                    shows.add(t);
                }

                return shows;
            }
        });
    }

    public List<Episode> getSeason(TVShow show, int season){
        return episodeRepository.findEpisodesBySeriesIdAndSeasonNumber(show.getId(), season);
    }

    public void saveShow(TVShow t){
        tvShowRepository.save(t);
    }

}