package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.repository.users.ShipOwnerRepository;
import com.example.BookingAppTeam05.model.users.ShipOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipOwnerService {
    private ShipOwnerRepository shipOwnerRepository;

    @Autowired
    public ShipOwnerService(ShipOwnerRepository shipOwnerRepository) {
        this.shipOwnerRepository = shipOwnerRepository;
    }

    public ShipOwnerService(){}

    public ShipOwner getShipOwnerWithShipsById(Long id) {
        return shipOwnerRepository.getShipOwnerWithShipsById(id);
    }
}
