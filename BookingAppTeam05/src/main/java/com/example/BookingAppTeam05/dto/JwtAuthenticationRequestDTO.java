package com.example.BookingAppTeam05.dto;

// DTO za login
public class JwtAuthenticationRequestDTO {

    private String email;
    private String password;

    public JwtAuthenticationRequestDTO() {
        super();
    }

    public JwtAuthenticationRequestDTO(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
