package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.*;

import java.util.Set;

public class CottageDTO extends BookingEntityDTO{
    private Set<Room> rooms;
    private CottageOwner cottageOwner;

    public CottageDTO() {
    }

    public CottageDTO(Cottage cottage) {
        super(cottage);
        this.rooms = cottage.getRooms();
        //this.cottageOwner = cottage.getCottageOwner();
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public CottageOwner getCottageOwner() {
        return cottageOwner;
    }

    public void setCottageOwner(CottageOwner cottageOwner) {
        this.cottageOwner = cottageOwner;
    }
}
