package com.example.BookingAppTeam05.dto.users;

import javax.validation.constraints.Size;

public class NewAccountRequestDTO {
    private UserDTO user;

    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String adminResponse;
    private boolean accepted;

    public NewAccountRequestDTO(UserDTO user, String adminResponse, boolean accepted) {
        this.user = user;
        this.adminResponse = adminResponse;
        this.accepted = accepted;
    }

    public NewAccountRequestDTO() {}

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
