package centigrade.reviews;
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
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private Environment env;

    @Autowired
    JdbcTemplate template;

    public void addReview(long contentId, long userId, double rating, String reviewText){
        Review r = new Review();
        r.setContentId(contentId);
        r.setRating(rating);
        r.setUserId(userId);
        r.setReviewText(reviewText);
        reviewRepository.save(r);
    }

    public List<Review> getReviewsByContent(long contentID){
        return reviewRepository.findReviewsByContentId(contentID);
    }
}
