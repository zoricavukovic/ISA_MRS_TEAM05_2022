package com.example.BookingAppTeam05.dto.users;

import com.example.BookingAppTeam05.dto.users.UserDTO;

// DTO koji enkapsulira generisani JWT i njegovo trajanje koji se vracaju klijentu
public class UserTokenStateDTO {

    private String accessToken;
    private Long expiresIn;
    private UserDTO user;

    public UserTokenStateDTO() {
        this.accessToken = null;
        this.expiresIn = null;
        this.user = null;
    }

    public UserTokenStateDTO(String accessToken, long expiresIn, UserDTO user) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}


