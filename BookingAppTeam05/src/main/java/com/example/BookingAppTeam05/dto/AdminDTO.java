package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.users.Admin;

public class AdminDTO extends UserDTO{
    public AdminDTO() {
    }

    public AdminDTO(Admin admin) {
        super(admin);
    }
}
