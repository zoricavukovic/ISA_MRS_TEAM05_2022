package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.CottageDTO;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.ShipDTO;
import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.RuleOfConduct;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.users.CottageOwner;
import com.example.BookingAppTeam05.model.users.ShipOwner;
import com.example.BookingAppTeam05.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/ships")
public class ShipController {

    private ShipService shipService;
    private BookingEntityService bookingEntityService;
    private PlaceService placeService;
    private UserService userService;
    private PricelistService pricelistService;
    private PictureService pictureService;

    @Autowired
    public ShipController(ShipService shipService, BookingEntityService bookingEntityService, PlaceService placeService,
                          UserService userService, PricelistService pricelistService, PictureService pictureService){
        this.shipService = shipService;
        this.bookingEntityService = bookingEntityService;
        this.placeService = placeService;
        this.userService = userService;
        this.pricelistService = pricelistService;
        this.pictureService = pictureService;
    }

    @GetMapping
    public ResponseEntity<List<ShipDTO>> getShips() {
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

    @GetMapping(value="/{id}")
    public ResponseEntity<ShipDTO> getShipById(@PathVariable Long id) {
        Ship ship = shipService.getShipById(id);
        if (ship == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ShipDTO shipDTO = new ShipDTO(ship);

        shipDTO.setPlace(ship.getPlace());
        if (ship.getRulesOfConduct() != null){
            shipDTO.setRulesOfConduct(ship.getRulesOfConduct());
        }
        shipDTO.setPictures(ship.getPictures());
        return new ResponseEntity<>(shipDTO, HttpStatus.OK);
    }

    @GetMapping(value="/editQue/{shipId}")
    @PreAuthorize("hasRole('ROLE_SHIP_OWNER')")
    public ResponseEntity<String> checkIfCanEdit(@PathVariable Long shipId) {
        Ship ship = shipService.findShipByShipIdWithOwner(shipId);
        if (ship == null) return new ResponseEntity<String>("Ship for editing is not found.", HttpStatus.BAD_REQUEST);
        if (shipService.checkExistActiveReservations(shipId)){
            return new ResponseEntity<String>("Cannot edit ship cause has reservations.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Ship can edit.", HttpStatus.OK);
    }

    @GetMapping(value="/owner/{id}")
    public ResponseEntity<List<ShipDTO>> getShipByOwnerId(@PathVariable Long id) {
        List<Ship> shipFound = shipService.getShipsByOwnerId(id);
        List<ShipDTO> shipDTOs = new ArrayList<>();

        for (Ship ship : shipFound) {
            ShipDTO sDTO = new ShipDTO(ship);
            sDTO.setPlace(ship.getPlace());
            sDTO.setPictures(ship.getPictures());
            shipDTOs.add(sDTO);
        }

        return ResponseEntity.ok(shipDTOs);
    }

}
