package centigrade.people;

import centigrade.movies.Movie;
import centigrade.TVShows.TVShow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    JdbcTemplate template;

    @Autowired
    private Environment env;

    public Person getPersonById(long id) {
        return personRepository.findPersonById(id);
    }

    public String getPersonPhotoURL() {
        String path = env.getProperty("person_photo_dir");
        return path;
    }

    public List<Person> findAllByIds(List<Long> ids) {
        ArrayList people = new ArrayList();

        for (Long id : ids) {
            Person p = personRepository.findPersonById(id);
            if (p != null) {
                people.add(p);
            }
        }

        return people;
    }

    public List<Person> getCastByMovie(Movie m) {
        return template.query("SELECT * FROM casttocontent WHERE contentId='" + m.getId() + "'AND Role='Actor'", new ResultSetExtractor<List<Person>>() {
            @Override
            public List<Person> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Person> cast = new ArrayList<Person>();

                while (rs.next()) {
                    Person p = personRepository.findPersonById(rs.getLong(2));
                    if (p != null) {
                        cast.add(p);
                    }
                }

                return cast;
            }
        });
    }

    public List<Person> getCastByTVShow(TVShow t) {
        return template.query("SELECT * FROM casttocontent WHERE contentId='" + t.getId() + "'AND Role='Actor'", new ResultSetExtractor<List<Person>>() {
            @Override
            public List<Person> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Person> cast = new ArrayList<Person>();

                while (rs.next()) {
                    Person p = personRepository.findPersonById(rs.getLong(2));
                    if (p != null) {
                        cast.add(p);
                    }
                }

                return cast;
            }
        });
    }


    public List<Person> getDirectorsByMovie(Movie m) {
        return template.query("SELECT * FROM casttocontent WHERE contentId='" + m.getId() + "'AND Role='Director'", new ResultSetExtractor<List<Person>>() {
            @Override
            public List<Person> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Person> directors = new ArrayList<Person>();

                while (rs.next()) {
                    Person p = personRepository.findPersonById(rs.getLong(2));
                    if (p != null) {
                        directors.add(p);
                    }
                }

                return directors;
            }
        });
    }

    public List<Person> getLikePeople(String token) {
        return template.query("select id from castmembers where firstname like \"%" + token + "%\" or lastname like \"%" + token + "%\"", new ResultSetExtractor<List<Person>>() {
            @Override
            public List<Person> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Person> cast = new ArrayList<Person>();

                while (rs.next()) {
                    Person p = personRepository.findPersonById(rs.getLong(1));
                    if (p != null) {
                        cast.add(p);
                    }
                }

                return cast;
            }
        });
    }


}
