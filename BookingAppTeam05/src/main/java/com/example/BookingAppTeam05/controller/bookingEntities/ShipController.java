package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.SearchParamsForEntity;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.dto.entities.ShipDTO;
import com.example.BookingAppTeam05.dto.users.ShipOwnerDTO;
import com.example.BookingAppTeam05.exception.ApiExceptionHandler;
import com.example.BookingAppTeam05.exception.ApiRequestException;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.users.ShipOwner;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.entities.ShipService;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/ships")
//@CrossOrigin
public class ShipController {

    private ShipService shipService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public ShipController(ShipService shipService, BookingEntityService bookingEntityService){
        this.shipService = shipService;
        this.bookingEntityService = bookingEntityService;
    }

    public ShipController(){}

    @GetMapping
    public ResponseEntity<List<ShipDTO>> getShips() {
        List<ShipDTO> shipDTOs = shipService.findAllShipsDTO();
        return new ResponseEntity<>(shipDTOs, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ShipDTO> getShipById(@PathVariable Long id) {
        ShipDTO shipDTO = shipService.getShipDTOById(id);
        return new ResponseEntity<>(shipDTO, HttpStatus.OK);
    }

    @GetMapping(value="/deleted/{id}")
    public ResponseEntity<ShipDTO> getShipByIdCanBeDeleted(@PathVariable Long id) {
        ShipDTO shipDTO = shipService.getShipDTOByIdCanBeDeleted(id);
        return new ResponseEntity<>(shipDTO, HttpStatus.OK);
    }

    @GetMapping(value="/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getShipsForView() {
        List<Ship> ships = shipService.findAll();
        return new ResponseEntity<>(getSearchedBookingEntitiesFromShips(ships), HttpStatus.OK);
    }

    @GetMapping(value="/view/forOwnerId/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getShipsForView(@PathVariable Long ownerId) {
        List<Ship> ships = shipService.findAllByOwnerId(ownerId);
        return new ResponseEntity<>(getSearchedBookingEntitiesFromShips(ships), HttpStatus.OK);
    }

    @GetMapping(value="/topRated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getTopRatedShipsForView() {
        return new ResponseEntity<>(bookingEntityService.findTopRated("ship"), HttpStatus.OK);
    }

    @GetMapping(value="/owner/{id}")
    public ResponseEntity<List<ShipDTO>> getShipByOwnerId(@PathVariable Long id) {
        List<ShipDTO> shipFound = shipService.getShipsDTOByOwnerId(id);
        return new ResponseEntity<>(shipFound, HttpStatus.OK);
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateShip(@RequestBody ShipDTO shipDTO, @PathVariable Long id) {
        shipService.updateShip(shipDTO, id);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PostMapping(value = "{idShipOwner}", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_SHIP_OWNER')")
    public ResponseEntity<String> saveShip(@PathVariable Long idShipOwner, @Valid @RequestBody ShipDTO shipDTO) {
        try{
            String retVal = shipService.saveShip(idShipOwner, shipDTO);
            return new ResponseEntity<>(retVal, HttpStatus.CREATED);
        }
        catch (ValidationException ex){
            throw new ApiRequestException("Someone is changing your values of new entity!");
        }
    }

    @PostMapping(value="/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getSearchedShips(@RequestBody SearchParamsForEntity searchParams) {
        try {
            List<SearchedBookingEntityDTO> shipDTOS = bookingEntityService.getSearchedBookingEntities(searchParams, "ship");
            return new ResponseEntity<>(shipDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    private List<SearchedBookingEntityDTO> getSearchedBookingEntitiesFromShips(List<Ship> ships) {
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Ship ship : ships) {
            SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(ship.getId());
            retVal.add(s);
        }
        return retVal;
    }
}
