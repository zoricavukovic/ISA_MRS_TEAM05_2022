package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="value", nullable = false)
    private float value;

    @Column(name="comment", length = 1024)
    private String comment;

    @Column(name="approved")
    private boolean approved;

    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    @Column(name="processed")
    private boolean processed;

    @Column(name="reviewDate", nullable = false)
    private LocalDateTime reviewDate;

    public Rating() {}

    public Rating(float value, String comment, boolean approved) {
        this.value = value;
        this.comment = comment;
        this.approved = approved;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Long getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
