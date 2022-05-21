package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.Complaint;

import javax.validation.constraints.Size;

public class ComplaintReviewDTO {
    private Long id;
    @Size(max = 500, message = "{validation.name.size.too_long}")
    private String clientComment;
    private boolean processed;
    @Size(max = 500, message = "{validation.name.size.too_long}")
    private String adminResponse;
    private ReservationDTO reservation;
    private UserDTO owner;

    public ComplaintReviewDTO() {}

    public ComplaintReviewDTO(Long id, @Size(max = 500, message = "{validation.name.size.too_long}") String clientComment, boolean processed, @Size(max = 250, message = "{validation.name.size.too_long}") String adminResponse, ReservationDTO reservation, UserDTO owner) {
        this.id = id;
        this.clientComment = clientComment;
        this.processed = processed;
        this.adminResponse = adminResponse;
        this.reservation = reservation;
        this.owner = owner;
    }

    public ComplaintReviewDTO(Complaint complaint, ReservationDTO reservation, UserDTO owner) {
        this.id = complaint.getId();
        this.clientComment = complaint.getDescription();
        this.processed = complaint.isProcessed();
        this.adminResponse = complaint.getAdminResponse();
        this.owner = owner;
        this.reservation = reservation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientComment() {
        return clientComment;
    }

    public void setClientComment(String clientComment) {
        this.clientComment = clientComment;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public ReservationDTO getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDTO reservation) {
        this.reservation = reservation;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }
}
