package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.SearchParamsForEntity;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/ships")
@CrossOrigin
public class ShipController {

    private ShipService shipService;
    private BookingEntityService bookingEntityService;
    private PlaceService placeService;
    private FishingEquipmentService fishingEquipmentService;
    private NavigationEquipmentService navigationEquipmentService;
    private UserService userService;
    private PricelistService pricelistService;
    private PictureService pictureService;

    @Autowired
    public ShipController(ShipService shipService, BookingEntityService bookingEntityService, PlaceService placeService,
                          UserService userService, PricelistService pricelistService, PictureService pictureService,
                          FishingEquipmentService fishingEquipmentService, NavigationEquipmentService navigationEquipmentService){
        this.shipService = shipService;
        this.bookingEntityService = bookingEntityService;
        this.placeService = placeService;
        this.userService = userService;
        this.pricelistService = pricelistService;
        this.pictureService = pictureService;
        this.fishingEquipmentService = fishingEquipmentService;
        this.navigationEquipmentService = navigationEquipmentService;
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
        return new ResponseEntity<>(getSearchedBookingEntitesFromShips(ships), HttpStatus.OK);
    }

    private List<SearchedBookingEntityDTO> getSearchedBookingEntitesFromShips(List<Ship> ships) {
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Ship ship : ships) {
            SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(ship.getId());
            retVal.add(s);
        }
        return retVal;
    }

    @GetMapping(value="/view/forOwnerId/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getShipsForView(@PathVariable Long ownerId) {
        List<Ship> ships = shipService.findAllByOwnerId(ownerId);
        return new ResponseEntity<>(getSearchedBookingEntitesFromShips(ships), HttpStatus.OK);
    }


    @GetMapping(value="/topRated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getTopRatedShipsForView() {
        List<SearchedBookingEntityDTO> retVal = bookingEntityService.findTopRated("ship");
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ShipDTO> getShipById(@PathVariable Long id) {
        Ship ship = shipService.getShipById(id);
        return getShipDTOResponseEntity(ship);
    }

    @GetMapping(value="/deleted/{id}")
    public ResponseEntity<ShipDTO> getShipByIdCanBeDeleted(@PathVariable Long id) {
        Ship ship = shipService.getShipByIdCanBeDeleted(id);
        return getShipDTOResponseEntity(ship);
    }

    private ResponseEntity<ShipDTO> getShipDTOResponseEntity(Ship ship) {
        if (ship == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ShipDTO shipDTO = new ShipDTO(ship);

        shipDTO.setPlace(ship.getPlace());
        if (ship.getRulesOfConduct() != null){
            shipDTO.setRulesOfConduct(ship.getRulesOfConduct());
        }
        shipDTO.setPictures(ship.getPictures());
        if (ship.getFishingEquipment() != null){
            shipDTO.setFishingEquipment(ship.getFishingEquipment());
        }
        if (ship.getNavigationalEquipment() != null){
            shipDTO.setNavigationalEquipment(ship.getNavigationalEquipment());
        }
        if(ship.getShipOwner() != null){
            shipDTO.setShipOwner(new ShipOwnerDTO(ship.getShipOwner()));
        }

        return new ResponseEntity<>(shipDTO, HttpStatus.OK);
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

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateShip(@RequestBody ShipDTO shipDTO, @PathVariable Long id) {
        String retVal = shipService.updateShip(shipDTO, id);
        if (retVal.isEmpty()) return new ResponseEntity<>(HttpStatus.CREATED);
        throw new ApiRequestException(retVal);


    }

    @PostMapping(value = "{idShipOwner}", consumes = "application/json")
    //@PreAuthorize("hasRole('ROLE_SHIP_OWNER')")
    public ResponseEntity<String> saveShip(@PathVariable Long idShipOwner, @Valid @RequestBody ShipDTO shipDTO) {
        try{
            String retVal = shipService.saveShip(idShipOwner, shipDTO);
            Integer.parseInt(retVal);
            return new ResponseEntity<>(retVal, HttpStatus.CREATED);
        }
        catch (NumberFormatException ex){
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
}
