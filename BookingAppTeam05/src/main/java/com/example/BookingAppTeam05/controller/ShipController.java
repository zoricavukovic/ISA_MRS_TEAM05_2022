package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.CottageDTO;
import com.example.BookingAppTeam05.dto.ShipDTO;
import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.model.Ship;
import com.example.BookingAppTeam05.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ships")
public class ShipController {

    private ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService){
        this.shipService = shipService;
    }

    @GetMapping
    public ResponseEntity<List<ShipDTO>> getCottages() {
        List<Ship> ships = shipService.findAll();

        List<ShipDTO> shipDTOs = new ArrayList<>();

        for (Ship ship:ships) {
            ShipDTO sDTO = new ShipDTO(ship);
            sDTO.setPlace(ship.getPlace());
            sDTO.setRulesOfConduct(ship.getRulesOfConduct());
            shipDTOs.add(sDTO);
        }

        return ResponseEntity.ok(shipDTOs);
    }

}
