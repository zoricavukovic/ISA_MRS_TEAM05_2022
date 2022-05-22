package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.DeleteAccountRequest;
import com.example.BookingAppTeam05.model.users.User;

import javax.persistence.*;
import javax.validation.constraints.Size;

public class DeleteAccountRequestDTO {
    public Long id;
    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String reason;
    private boolean processed;
    private boolean accepted;
    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String adminResponse;
    private UserDTO user;

    public DeleteAccountRequestDTO(Long id, String reason, boolean processed, boolean accepted, String adminResponse, UserDTO user) {
        this.id = id;
        this.reason = reason;
        this.processed = processed;
        this.accepted = accepted;
        this.adminResponse = adminResponse;
        this.user = user;
    }

    public DeleteAccountRequestDTO(DeleteAccountRequest deleteAccountRequest, UserDTO user) {
        this.id = deleteAccountRequest.getId();
        this.reason = deleteAccountRequest.getReason();
        this.processed = deleteAccountRequest.isProcessed();
        this.accepted = deleteAccountRequest.isAccepted();
        this.adminResponse = deleteAccountRequest.getAdminResponse();
        this.user = user;
    }

    public DeleteAccountRequestDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
