package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.Rating;
import com.example.BookingAppTeam05.model.Reservation;

import javax.persistence.*;
import java.time.LocalDateTime;

public class RatingDTO {
    private Long id;
    private float value;
    private String comment;
    private LocalDateTime reviewDate;
    private ReservationDTO reservation;

    public RatingDTO() {
    }

    public RatingDTO(Rating rating) {
        this.id = rating.getId();
        this.value = rating.getValue();
        this.comment = rating.getComment();
        this.reservation = new ReservationDTO(rating.getReservation());
        this.reviewDate = rating.getReviewDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ReservationDTO getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDTO reservation) {
        this.reservation = reservation;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
}
