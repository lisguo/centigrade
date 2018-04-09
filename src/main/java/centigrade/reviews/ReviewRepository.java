package centigrade.reviews;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Long>{
    Review findReviewById(long id);
    List<Review> findReviewsByUserId(long userId);
    List<Review> findReviewsByContentId(long contentId);
    Review save(Review r);
    void delete(Review r);
}
