package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.entities.ShipDTO;
import com.example.BookingAppTeam05.exception.ApiRequestException;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.users.ShipOwner;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.model.repository.entities.ShipRepository;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<Ship> findAll() {
        return shipRepository.findAll();
    }

    public List<Ship> getShipsByOwnerId(Long id) {
        return shipRepository.getShipsByOwnerId(id);
    }

    public User getShipOwnerOfShipId(Long id) {
        return this.shipRepository.getShipOwnerOfShipId(id);
    }
    public Ship getShipById(Long id) {
        return shipRepository.getShipById(id);
    }

    public Ship getShipByIdCanBeDeleted(Long id) {
        return shipRepository.getShipByIdCanBeDeleted(id);
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
    public String updateShip(ShipDTO shipDTO, Long id){
        try {
            if (reservationService.findAllActiveReservationsForEntityid(id).size() != 0)
                return "Cant update ship because there exist active reservations";

            Ship ship = getShipById(id);
            if (ship == null)  return "Cant find ship with id " + id + ".";
            if (ship.getVersion() != shipDTO.getVersion()) return "Conflict seems to have occurred, someone changed your ship data before you. Please refresh page and try again";
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
            if (p == null) return "Cant find chosen place.";
            Place place = placeService.getPlaceByZipCode(p.getZipCode());
            ship.setPlace(place);

            Set<RuleOfConduct> rules = new HashSet<RuleOfConduct>();
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
            if (save(ship) == null ) return "Conflict seems to have occurred, someone changed your ship before you. Please refresh page and try again";
        } catch (ObjectOptimisticLockingFailureException e) {
            return "Conflict seems to have occurred, someone changed your ship before you. Please refresh page and try again";
        }
        return "";
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
        if (shipDTO.getPlace() == null) return "Cant find place.";
        Place place1 = placeService.getPlaceByZipCode(shipDTO.getPlace().getZipCode());
        if (place1 == null) return "Cant find place with zip code: " + shipDTO.getPlace().getZipCode();
        ship.setPlace(place1);
        ShipOwner shipOwner = (ShipOwner) userService.findUserById(idShipOwner);
        if (shipOwner == null) return "Cant find ship owner with id: " + idShipOwner;
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
        if (ship != null) return ship.getId().toString();
        return "Entity cant save. Try again later!";

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
}
