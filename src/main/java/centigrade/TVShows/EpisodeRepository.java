package centigrade.TVShows;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends CrudRepository<Episode, Long>{
    List<Episode> findAll();
    Episode findEpisodeById(long id);

    List<Episode> findEpisodesBySeriesIdAndSeasonNumber(long seriesId, int seasonNumber);

    Episode save(Episode e);

    void delete(Episode e);
    void deleteEpisodeById(long id);
}
