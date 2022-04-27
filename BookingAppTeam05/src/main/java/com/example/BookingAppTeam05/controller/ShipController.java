package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.ShipDTO;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.service.BookingEntityService;
import com.example.BookingAppTeam05.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ships")
public class ShipController {

    private ShipService shipService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public ShipController(ShipService shipService, BookingEntityService bookingEntityService){
        this.shipService = shipService;
        this.bookingEntityService = bookingEntityService;
    }

    @GetMapping
    public ResponseEntity<List<ShipDTO>> getShips() {
        List<Ship> ships = shipService.findAll();

        System.out.println("-=----------------");
        for (Ship ship : ships) {
            System.out.println(ship.getName() + ship.getEngineNum());
        }
        System.out.println("-=----------------");

        List<ShipDTO> shipDTOs = new ArrayList<>();

        for (Ship ship:ships) {
            ShipDTO sDTO = new ShipDTO(ship);
            sDTO.setPlace(ship.getPlace());
            sDTO.setRulesOfConduct(ship.getRulesOfConduct());
            shipDTOs.add(sDTO);
        }

        return ResponseEntity.ok(shipDTOs);
    }


    @GetMapping(value="/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getShipsForView() {
        List<Ship> ships = shipService.findAll();
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Ship ship : ships) {
            SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(ship.getId());
            retVal.add(s);
        }
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }


}
