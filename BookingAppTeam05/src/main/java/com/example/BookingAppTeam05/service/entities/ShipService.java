package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.entities.ShipDTO;
import com.example.BookingAppTeam05.dto.users.ShipOwnerDTO;
import com.example.BookingAppTeam05.exception.*;
import com.example.BookingAppTeam05.exception.database.CreateItemException;
import com.example.BookingAppTeam05.exception.database.EditItemException;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.users.ShipOwner;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.repository.entities.ShipRepository;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class ShipService {

    private ShipRepository shipRepository;
    private PlaceService placeService;
    private RuleOfConductService ruleOfConductService;
    private PictureService pictureService;
    private ReservationService reservationService;
    private NavigationEquipmentService navigationEquipmentService;
    private FishingEquipmentService fishingEquipmentService;
    private UserService userService;

    @Autowired
    public ShipService(ShipRepository shipRepository, RuleOfConductService ruleOfConductService,
                       PictureService pictureService, ReservationService reservationService,
                       NavigationEquipmentService navigationEquipmentService,
                       FishingEquipmentService fishingEquipmentService, PlaceService placeService, UserService userService){
        this.shipRepository = shipRepository;
        this.ruleOfConductService = ruleOfConductService;
        this.pictureService = pictureService;
        this.reservationService = reservationService;
        this.navigationEquipmentService = navigationEquipmentService;
        this.fishingEquipmentService = fishingEquipmentService;
        this.placeService = placeService;
        this.userService = userService;
    }

    public ShipService(){}

    public Ship getShipById(Long id) {
        return shipRepository.getShipById(id);
    }

    public Ship getShipByIdCanBeDeleted(Long id) {
        return shipRepository.getShipByIdCanBeDeleted(id);
    }

    public ShipDTO getShipDTOById(Long id) {
        Ship ship = getShipById(id);
        if (ship == null)
            throw new ItemNotFoundException("Can't find ship with id: " + id);
        return getShipDTO(ship);
    }

    public ShipDTO getShipDTOByIdCanBeDeleted(Long id) {
        Ship ship = getShipByIdCanBeDeleted(id);
        if (ship == null)
            throw new ItemNotFoundException("Can't find ship with id: " + id);
        return getShipDTO(ship);
    }

    private ShipDTO getShipDTO(Ship ship) {
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

        return shipDTO;
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

    public List<ShipDTO> getShipsDTOByOwnerId(Long id){
        List<Ship> shipFound = getShipsByOwnerId(id);
        List<ShipDTO> shipDTOs = new ArrayList<>();
        for (Ship ship : shipFound) {
            ShipDTO sDTO = new ShipDTO(ship);
            sDTO.setPlace(ship.getPlace());
            sDTO.setPictures(ship.getPictures());
            shipDTOs.add(sDTO);
        }
        return shipDTOs;
    }

    public Ship save(Ship ship) {
        try {
            return shipRepository.save(ship);
        } catch (ObjectOptimisticLockingFailureException e) {
            return null;
        }
    }

    public Ship findById(Long id) {
        Optional<Ship> ship = shipRepository.findById(id);
        return ship.orElse(null);
    }

    @Transactional
    public void updateShip(ShipDTO shipDTO, Long id){
        try {
            if (reservationService.findAllActiveReservationsForEntityid(id).size() != 0)
                throw new EditItemException("Can't update ship because there exist active reservations");

            Ship ship = getShipById(id);
            if (ship == null)  throw new ItemNotFoundException("Cant find ship with id: " + id);
            if (ship.getVersion() != shipDTO.getVersion())
                throw new ConflictException("Conflict seems to have occurred, someone changed your ship data before you. Please refresh page and try again!");
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
            if (p == null) throw new ItemNotFoundException("Can't find chosen place");
            Place place = placeService.getPlaceByZipCode(p.getZipCode());
            ship.setPlace(place);

            Set<RuleOfConduct> rules = new HashSet<>();
            Ship oldShip = getShipById(id);
            tryToEditShipRulesOfConduct(shipDTO, oldShip, rules);
            ship.setRulesOfConduct(rules);

            Set<NavigationEquipment> navigationEquipments = new HashSet<>();
            oldShip = getShipById(id);
            tryToEditNavigationEquipment(shipDTO, oldShip, navigationEquipments);
            ship.setNavigationalEquipment(navigationEquipments);
            Set<FishingEquipment> fishingEquipment = fishingEquipmentService.createEquipmentFromDTO(shipDTO.getFishingEquipment());
            ship.setFishingEquipment(fishingEquipment);
            pictureService.setNewImagesForBookingEntity(ship, shipDTO.getImages());
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConflictException("Conflict seems to have occurred, someone changed your ship data before you. Please refresh page and try again!");
        }
    }

    @Transactional
    public String saveShip(Long idShipOwner, ShipDTO shipDTO){
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
        if (shipDTO.getPlace() == null) throw new ItemNotFoundException("Can't create ship without set place");
        Place place1 = placeService.getPlaceByZipCode(shipDTO.getPlace().getZipCode());
        if (place1 == null)
            throw new ItemNotFoundException("Can't find place with zip code: " + shipDTO.getPlace().getZipCode());
        ship.setPlace(place1);
        ShipOwner shipOwner = (ShipOwner) userService.findUserById(idShipOwner);
        if (shipOwner == null) throw new ItemNotFoundException("Can't find ship owner with id: " + idShipOwner);
        ship.setShipOwner(shipOwner);
        ship.setRulesOfConduct(shipDTO.getRulesOfConduct());
        if (!shipDTO.getImages().isEmpty()) {
            Set<Picture> createdPictures = pictureService.createPicturesFromDTO(shipDTO.getImages());
            ship.setPictures(createdPictures);
        }
        Set<NavigationEquipment> navigationEquipments = new HashSet<>();
        tryToEditNavigationEquipment(shipDTO, navigationEquipments);
        ship.setNavigationalEquipment(shipDTO.getNavigationalEquipment());
        ship.setFishingEquipment(shipDTO.getFishingEquipment());

        ship.setVersion(0);
        ship.setLocked(false);
        ship = save(ship);
        if (ship == null)
            throw new CreateItemException("Can't create new cottage. Try again!");
        return ship.getId().toString();
    }

    public void tryToEditShipRulesOfConduct(ShipDTO shipDTO, Ship oldShip, Set<RuleOfConduct> rules) {
        for (RuleOfConduct oldRule: oldShip.getRulesOfConduct()) {
            boolean found = false;
            for (RuleOfConduct rule: shipDTO.getRulesOfConduct()){
                if (rule.getRuleName().equals(oldRule.getRuleName())){

                    if (rule.isAllowed() != oldRule.isAllowed()) {
                        ruleOfConductService.updateAllowedRuleById(oldRule.getId(), !oldRule.isAllowed());
                        oldRule.setAllowed(rule.isAllowed());
                        rules.add(oldRule);
                    }
                    else{
                        rules.add(oldRule);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                ruleOfConductService.deleteRuleById(oldRule.getId()); }

        }
        for (RuleOfConduct rule: shipDTO.getRulesOfConduct()){
            boolean found = false;
            for (RuleOfConduct addedRule: rules){
                if (rule.getRuleName().equals(addedRule.getRuleName())){
                    found = true;
                    break;
                }
            }
            if (!found) { rules.add(rule); }
        }
    }

    public void tryToEditNavigationEquipment(ShipDTO shipDTO, Ship oldShip, Set<NavigationEquipment> navigationEquipments) {
        for (NavigationEquipment oldNav: oldShip.getNavigationalEquipment()) {
            boolean found = false;
            for (NavigationEquipment nav: shipDTO.getNavigationalEquipment()){
                if (nav.getName().equals(oldNav.getName())){
                    navigationEquipments.add(oldNav);
                    navigationEquipmentService.save(oldNav);
                    found = true;
                    break;
                }
            }
            if (!found) {
                navigationEquipmentService.deleteNavigationEquipment(oldNav.getId());
            }
        }
        for (NavigationEquipment rule: shipDTO.getNavigationalEquipment()){
            boolean found = false;
            for (NavigationEquipment addedNav: navigationEquipments){
                if (rule.getName().equals(addedNav.getName())){
                    found = true;
                    break;
                }
            }
            if (!found) {
                navigationEquipments.add(rule);
                navigationEquipmentService.save(rule);
            }
        }
    }

    public void tryToEditNavigationEquipment(ShipDTO shipDTO, Set<NavigationEquipment> navigationEquipments) {
        for (NavigationEquipment rule: shipDTO.getNavigationalEquipment()){
            boolean found = false;
            for (NavigationEquipment addedNav: navigationEquipments){
                if (rule.getName().equals(addedNav.getName())){
                    found = true;
                    break;
                }
            }
            if (!found) {
                navigationEquipments.add(rule);
                navigationEquipmentService.save(rule);
            }
        }
    }

    public List<Ship> findAllByOwnerId(Long id) {
        List<Ship> all = findAll();
        List<Ship> retVal = new ArrayList<>();
        for (Ship s : all) {
            if (s.getShipOwner().getId().equals(id))
                retVal.add(s);
        }
        return retVal;
    }

    public List<ShipDTO> findAllShipsDTO() {
        List<Ship> ships = findAll();
        List<ShipDTO> shipDTOs = new ArrayList<>();
        for (Ship ship:ships) {
            ShipDTO sDTO = new ShipDTO(ship);
            sDTO.setPlace(ship.getPlace());
            sDTO.setRulesOfConduct(ship.getRulesOfConduct());
            shipDTOs.add(sDTO);
        }
        return shipDTOs;
    }
}
