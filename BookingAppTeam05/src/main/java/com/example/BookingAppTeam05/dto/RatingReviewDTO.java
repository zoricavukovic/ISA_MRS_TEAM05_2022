package com.example.BookingAppTeam05.dto;


import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.Rating;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class RatingReviewDTO {
    private Long id;
    private UserDTO owner;
    private float value;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime reviewDate;
    private ReservationDTO reservation;
    private boolean processed;

    public RatingReviewDTO() {}

    public RatingReviewDTO(Long id, UserDTO owner, float value, String comment, ReservationDTO reservation, boolean processed) {
        this.id = id;
        this.owner = owner;
        this.value = value;
        this.comment = comment;
        this.reservation = reservation;
        this.processed = processed;
    }

    public RatingReviewDTO(Rating rating, ReservationDTO reservation, UserDTO owner) {
        this.id = rating.getId();
        this.value = rating.getValue();
        this.comment = rating.getComment();
        this.processed = rating.isProcessed();
        this.reservation = reservation;
        this.reviewDate = rating.getReviewDate();
        this.owner = owner;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
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

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
