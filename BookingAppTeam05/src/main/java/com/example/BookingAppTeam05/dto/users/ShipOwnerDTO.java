package com.example.BookingAppTeam05.dto.users;

import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.users.ShipOwner;

public class ShipOwnerDTO extends UserDTO {
    public ShipOwnerDTO() {
    }

    public ShipOwnerDTO(ShipOwner shipOwner) {
        super(shipOwner);
    }
}
