package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.Report;

public class CreatedReportReviewDTO {
    private Long id;
    private boolean clientCome;
    private String ownerComment;
    private boolean penalizeClient;
    private boolean processed;
    private String adminResponse;
    private boolean adminPenalizeClient;
    private ReservationDTO reservation;
    private UserDTO owner;

    public CreatedReportReviewDTO() {}

    public CreatedReportReviewDTO(Report report, ReservationDTO reservation, UserDTO owner) {
        this.id = report.getId();
        this.clientCome = report.isComeClient();
        this.ownerComment = report.getComment();
        this.penalizeClient = report.isPenalizeClient();
        this.processed = report.isProcessed();
        this.adminResponse = report.getAdminResponse();
        this.reservation = reservation;
        this.owner = owner;
        this.adminPenalizeClient = report.isAdminPenalizeClient();
    }

    public boolean isAdminPenalizeClient() {
        return adminPenalizeClient;
    }

    public void setAdminPenalizeClient(boolean adminPenalizeClient) {
        this.adminPenalizeClient = adminPenalizeClient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isClientCome() {
        return clientCome;
    }

    public void setClientCome(boolean clientCome) {
        this.clientCome = clientCome;
    }

    public String getOwnerComment() {
        return ownerComment;
    }

    public void setOwnerComment(String ownerComment) {
        this.ownerComment = ownerComment;
    }

    public boolean isPenalizeClient() {
        return penalizeClient;
    }

    public void setPenalizeClient(boolean penalizeClient) {
        this.penalizeClient = penalizeClient;
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
