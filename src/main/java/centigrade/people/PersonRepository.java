package centigrade.people;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long>{
    List<Person> findAll();
    Person findPersonById(long id);

    Person save(Person p);

    void delete(Person p);
    void deletePersonById(long id);
}


