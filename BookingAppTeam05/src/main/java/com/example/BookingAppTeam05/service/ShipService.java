package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class ShipService {

    private ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository){
        this.shipRepository = shipRepository;

    }

    public List<Ship> findAll() {
        return shipRepository.findAll();
    }

    public List<Ship> getShipsByOwnerId(Long id) {
        return shipRepository.getShipsByOwnerId(id);
    }

    public User getShipOwnerOfShipId(Long id) {
        return this.shipRepository.getShipOwnerOfShipId(id);
    }
}
