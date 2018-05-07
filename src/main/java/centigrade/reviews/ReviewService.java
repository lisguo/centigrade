package centigrade.reviews;

import centigrade.TVShows.TVShow;
import centigrade.TVShows.TVShowService;
import centigrade.movies.Movie;
import centigrade.movies.MovieService;
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
    private MovieService movieService;
    @Autowired
    private TVShowService tvShowService;

    @Autowired
    private Environment env;

    @Autowired
    JdbcTemplate template;

    public List<Review> getAllReviewsOrderedById(){
        return reviewRepository.findAllByOrderById();
    }

    public void addReview(long contentId, long userId, double rating, String reviewText) {
        Review r = new Review();
        r.setContentId(contentId);
        r.setRating(rating);
        r.setUserId(userId);
        r.setReviewText(reviewText);
        reviewRepository.save(r);
    }

    public void editReview(Review r, double newRating, String newReviewText){ ;
        r.setRating(newRating);

        if(newReviewText.trim().equals("")){
            r.setReviewText(null);
        }else{
            r.setReviewText(newReviewText);
        }

        reviewRepository.save(r);
    }

    public void deleteReview(Review r) {
        Movie m = movieService.getMovieById(r.getContentId());
        TVShow t = tvShowService.getTVShowById(r.getContentId());

        if (m != null) {
            m.setTimesRated(m.getTimesRated() - 1);
            m.setRatingSum(m.getRatingSum() - r.getRating());
            movieService.saveMovie(m);
        } else if (t != null) {
            t.setTimesRated(t.getTimesRated() - 1);
            t.setRatingSum(t.getRatingSum() - r.getRating());
            tvShowService.saveShow(t);
        }

        reviewRepository.delete(r);
    }

    public List<Review> getReviewsByContent(long contentID) {
        return reviewRepository.findReviewsByContentId(contentID);
    }

    public List<Review> getReviewsByUser(long userID) {
        return reviewRepository.findReviewsByUserId(userID);
    }

    public List<Review> getReviewsByUserAndContent(long userId, long contentId) {
        return reviewRepository.findReviewsByUserIdAndContentId(userId, contentId);
    }

    public Review getReviewById(long id) {
        return reviewRepository.findReviewById(id);
    }
}
