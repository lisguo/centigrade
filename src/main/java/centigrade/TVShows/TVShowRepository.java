package centigrade.TVShows;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TVShowRepository extends CrudRepository<TVShow, Long> {
    List<TVShow> findAll();

    TVShow findTVShowById(long id);

    TVShow save(TVShow t);

    void delete(TVShow t);
}
