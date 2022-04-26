package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.EntityType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CottageDTO extends BookingEntityDTO{
    private Set<Room> rooms = new HashSet<>();
    @Valid
    @NotNull
    private List<NewImageDTO> images;

    public CottageDTO() {
    }

    public CottageDTO(Cottage cottage) {
        super(cottage);

    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public List<NewImageDTO> getImages() {
        return images;
    }

    public void setImages(List<NewImageDTO> images) {
        this.images = images;
    }
}
