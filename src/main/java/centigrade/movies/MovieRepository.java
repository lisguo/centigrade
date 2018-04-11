package centigrade.movies;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findAll();

    List<Movie> findAllByOrderByYear();

    List<Movie> findAllByOrderByTitle();

    Movie findMovieById(long id);

    Movie save(Movie m);

    void delete(Movie m);
}
