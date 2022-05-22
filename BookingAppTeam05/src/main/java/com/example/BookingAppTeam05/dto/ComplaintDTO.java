package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.Complaint;
import com.example.BookingAppTeam05.model.Reservation;

import javax.persistence.*;

public class ComplaintDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="description", nullable = false)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    public ReservationDTO reservation;

    public ComplaintDTO() {
    }

    public ComplaintDTO(String description, ReservationDTO reservation) {
        this.description = description;
        this.reservation = reservation;
    }

    public ComplaintDTO(Complaint complaint) {
        this.id = complaint.getId();
        this.description = complaint.getDescription();
        this.reservation = new ReservationDTO(complaint.getReservation());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReservationDTO getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDTO reservation) {
        this.reservation = reservation;
    }
}
