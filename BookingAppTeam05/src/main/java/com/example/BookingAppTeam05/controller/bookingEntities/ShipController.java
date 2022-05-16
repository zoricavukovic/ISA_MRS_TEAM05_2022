package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.SearchParamsForEntity;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.entities.ShipDTO;
import com.example.BookingAppTeam05.model.*;
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
        if (ship.getFishingEquipment() != null){
            shipDTO.setFishingEquipment(ship.getFishingEquipment());
        }
        if (ship.getNavigationalEquipment() != null){
            shipDTO.setNavigationalEquipment(ship.getNavigationalEquipment());
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
    @Transactional
    public ResponseEntity<String> updateCottage(@RequestBody ShipDTO shipDTO, @PathVariable Long id) {
        if (bookingEntityService.checkExistActiveReservationForEntityId(id))
            return new ResponseEntity<>("Cant update ship because there exist active reservations", HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getShipById(id);
        if (ship == null) return new ResponseEntity<>("Cant find ship with id " + id + ".", HttpStatus.BAD_REQUEST);
        ship.setName(shipDTO.getName());
        ship.setAddress(shipDTO.getAddress());
        ship.setPromoDescription(shipDTO.getPromoDescription());
        ship.setLength(shipDTO.getLength());
        ship.setEngineNum(shipDTO.getEngineNum());
        ship.setEnginePower(shipDTO.getEnginePower());
        ship.setMaxNumOfPersons(shipDTO.getMaxNumOfPersons());
        ship.setMaxSpeed(shipDTO.getMaxSpeed());
        ship.setEntityCancelationRate(shipDTO.getEntityCancelationRate());
        ship.setShipType(shipDTO.getShipType());
        ship.setEntityType(EntityType.SHIP);

        Place p = shipDTO.getPlace();
        if (p == null) return new ResponseEntity<>("Cant find chosen place.", HttpStatus.BAD_REQUEST);
        Place place = placeService.getPlaceByZipCode(p.getZipCode());
        ship.setPlace(place);

        Set<RuleOfConduct> rules = new HashSet<RuleOfConduct>();
        Ship oldShip = shipService.getShipById(id);
        shipService.tryToEditShipRulesOfConduct(shipDTO, oldShip, rules);
        ship.setRulesOfConduct(rules);

        Set<NavigationEquipment> navigationEquipments = new HashSet<>();
        oldShip = shipService.getShipById(id);
        shipService.tryToEditNavigationEquipment(shipDTO, oldShip, navigationEquipments);
        ship.setNavigationalEquipment(navigationEquipments);
        Set<FishingEquipment> fishingEquipment = fishingEquipmentService.createEquipmentFromDTO(shipDTO.getFishingEquipment());
        ship.setFishingEquipment(fishingEquipment);
        bookingEntityService.setNewImagesForBookingEntity(ship, shipDTO.getImages());
        ship = shipService.save(ship);
        return new ResponseEntity<>(ship.getId().toString(), HttpStatus.OK);
    }

    @PostMapping(value = "{idShipOwner}", consumes = "application/json")
    @Transactional
    //@PreAuthorize("hasRole('ROLE_SHIP_OWNER')")
    public ResponseEntity<String> saveShip(@PathVariable Long idShipOwner, @Valid @RequestBody ShipDTO shipDTO) {
        System.out.println("CAOOOO");
        Ship ship = new Ship();
        ship.setName(shipDTO.getName());
        ship.setAddress(shipDTO.getAddress());
        ship.setPromoDescription(shipDTO.getPromoDescription());
        ship.setLength(shipDTO.getLength());
        ship.setEngineNum(shipDTO.getEngineNum());
        ship.setEnginePower(shipDTO.getEnginePower());
        ship.setMaxNumOfPersons(shipDTO.getMaxNumOfPersons());
        ship.setMaxSpeed(shipDTO.getMaxSpeed());
        ship.setEntityCancelationRate(shipDTO.getEntityCancelationRate());
        ship.setEntityType(EntityType.SHIP);
        ship.setShipType(shipDTO.getShipType());
        System.out.println("CAOOOO");
        if (shipDTO.getPlace() == null) return new ResponseEntity<>("Cant find place.", HttpStatus.BAD_REQUEST);
        Place place1 = placeService.getPlaceByZipCode(shipDTO.getPlace().getZipCode());
        if (place1 == null) return new ResponseEntity<>("Cant find place with zip code: " + shipDTO.getPlace().getZipCode(), HttpStatus.BAD_REQUEST);
        ship.setPlace(place1);
        System.out.println("CAOOOO");
        ShipOwner shipOwner = (ShipOwner) userService.findUserById(idShipOwner);
        if (shipOwner == null) return new ResponseEntity<>("Cant find ship owner with id: " + idShipOwner, HttpStatus.BAD_REQUEST);
        ship.setShipOwner(shipOwner);
        System.out.println("CAOOOO");
        ship.setRulesOfConduct(shipDTO.getRulesOfConduct());
        if (!shipDTO.getImages().isEmpty()) {
            Set<Picture> createdPictures = pictureService.createPicturesFromDTO(shipDTO.getImages());
            ship.setPictures(createdPictures);
        }
        System.out.println("CAOOOO");
        Set<NavigationEquipment> navigationEquipments = new HashSet<>();
        shipService.tryToEditNavigationEquipment(shipDTO, navigationEquipments);
        ship.setNavigationalEquipment(shipDTO.getNavigationalEquipment());
        System.out.println("CAOOOO NAV");
        ship.setFishingEquipment(shipDTO.getFishingEquipment());
        System.out.println(" CAOOOO fishing");
        ship = shipService.save(ship);

        return new ResponseEntity<>(ship.getId().toString(), HttpStatus.CREATED);
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
