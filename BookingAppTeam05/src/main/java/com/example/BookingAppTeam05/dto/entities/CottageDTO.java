package com.example.BookingAppTeam05.dto.entities;

import com.example.BookingAppTeam05.dto.NewImageDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.CottageOwnerDTO;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Cottage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CottageDTO extends BookingEntityDTO {
    private Set<Room> rooms = new HashSet<>();
    @Valid
    private List<NewImageDTO> images = new ArrayList<>();
    private CottageOwnerDTO cottageOwnerDTO;

    public CottageDTO() {
    }

    public CottageDTO(Cottage cottage) {
        super(cottage);

    }

    public void setFetchedProperties(Cottage cottage){
        super.setFetchedProperties(cottage);
        this.rooms = cottage.getRooms();
        this.cottageOwnerDTO = new CottageOwnerDTO(cottage.getCottageOwner());
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

    public CottageOwnerDTO getCottageOwnerDTO() {
        return cottageOwnerDTO;
    }

    public void setCottageOwnerDTO(CottageOwnerDTO cottageOwnerDTO) {
        this.cottageOwnerDTO = cottageOwnerDTO;
    }
}
