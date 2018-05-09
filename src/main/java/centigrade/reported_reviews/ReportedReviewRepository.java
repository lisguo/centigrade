package centigrade.reported_reviews;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportedReviewRepository extends CrudRepository<ReportedReview, Long> {

    List<ReportedReview> findAll();
    void delete(ReportedReview rr);
    ReportedReview findReportedReviewByid(long id);
}