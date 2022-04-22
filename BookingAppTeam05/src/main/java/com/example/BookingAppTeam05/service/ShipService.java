package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Ship;
import com.example.BookingAppTeam05.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
