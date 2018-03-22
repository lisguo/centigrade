package centigrade.people;

import centigrade.movies.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private Environment env;

    public Person getPersonById(long id){
        return personRepository.findPersonById(id);
    }

    public String getPersonPhotoURL(Person p){
        String name =  p.getName();
        //name = name.replace(":", "_");
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

}
