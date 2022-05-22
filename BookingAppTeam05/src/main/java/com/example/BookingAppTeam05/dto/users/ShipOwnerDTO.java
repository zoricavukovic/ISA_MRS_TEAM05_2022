package com.example.BookingAppTeam05.dto.users;

import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.users.ShipOwner;

public class ShipOwnerDTO extends UserDTO {
    private boolean captain;

    public ShipOwnerDTO() {
    }

    public ShipOwnerDTO(ShipOwner shipOwner) {
        super(shipOwner);
    }

    public boolean isCaptain() {
        return captain;
    }

    public void setCaptain(boolean captain) {
        this.captain = captain;
    }
}
