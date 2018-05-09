package centigrade.reported_reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ReportedReviewService {
    @Autowired
    ReportedReviewRepository reportedReviewRepository;

    public List<ReportedReview> getAllReportedReviews(){
        return reportedReviewRepository.findAll();
    }

    public void deleteReportedRevew(ReportedReview rr){
        reportedReviewRepository.delete(rr);
    }

    public ReportedReview getReportedReviewById(long id){ return reportedReviewRepository.findReportedReviewByid(id);}
}

