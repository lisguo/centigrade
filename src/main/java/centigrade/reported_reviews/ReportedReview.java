package centigrade.reported_reviews;

import centigrade.reviews.Review;

import javax.persistence.*;

@Entity
@Table(name = "reportedreviews")
public class ReportedReview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String message;
    private long reporterid;
    private long reviewid;

    @Transient
    private Review review;


    @Column(name="id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name="reporterid")
    public long getReporterid() {
        return reporterid;
    }

    public void setReporterid(long reporterid) {
        this.reporterid = reporterid;
    }

    @Column(name="reviewid")
    public long getReviewid() {
        return reviewid;
    }

    public void setReviewid(long reviewid) {
        this.reviewid = reviewid;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
