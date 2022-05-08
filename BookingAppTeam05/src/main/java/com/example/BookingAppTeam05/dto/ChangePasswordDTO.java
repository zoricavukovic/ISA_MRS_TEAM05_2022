package com.example.BookingAppTeam05.dto;

public class ChangePasswordDTO {
    private Long id;
    private String currPassword;
    private String newPassword;

    public ChangePasswordDTO() {}

    public ChangePasswordDTO(Long id, String currPassword, String newPassword) {
        this.id = id;
        this.currPassword = currPassword;
        this.newPassword = newPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrPassword() {
        return currPassword;
    }

    public void setCurrPassword(String currPassword) {
        this.currPassword = currPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
