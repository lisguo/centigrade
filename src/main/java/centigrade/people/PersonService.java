package centigrade.people;

import centigrade.movies.Movie;

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

    public Person getPersonById(long id){
        return personRepository.findPersonById(id);
    }

    public String getPersonPhotoURL(Person p){
        String name =  p.getName();
        name = name.replace(":", "_");
        String path = env.getProperty("person_photo_dir") + name + ".jpg";
        return path;
    }

    public List<Person> findAllByIds(List<Long> ids){
        ArrayList people = new ArrayList();

        for(Long id : ids){
            Person p = personRepository.findPersonById(id);
            people.add(p);
        }

        return people;
    }

    public List<Person> getCastByMovie(Movie m){
        return template.query("SELECT * FROM actor_in_movie WHERE movie_id='" + m.getId() +"'", new ResultSetExtractor<List<Person>>() {
            @Override
            public List<Person> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Person> cast = new ArrayList<Person>();

                while(rs.next())
                {
                    Person p = personRepository.findPersonById(rs.getInt(1));
                    cast.add(p);
                }

                return cast;
            }
        });
    }



}
