package centigrade.reviews;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findReviewsByUserId(long userId);

    List<Review> findReviewsByContentId(long contentId);

    List<Review> findReviewsByUserIdAndContentId(long userId, long contentId);

    Review save(Review r);

    void delete(Review r);
}
