package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.RuleOfConduct;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.users.CottageOwner;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.model.repository.entities.CottageRepository;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CottageService {
    private CottageRepository cottageRepository;
    private ReservationService reservationService;
    private PictureService pictureService;
    private RoomService roomService;
    private RuleOfConductService ruleOfConductService;
    private PlaceService placeService;
    private UserService userService;

    @Autowired
    public CottageService(CottageRepository cottageRepository, ReservationService reservationService,
                          PictureService pictureService, RoomService roomService, RuleOfConductService ruleOfConductService,
                          PlaceService placeService, UserService userService) {
        this.cottageRepository = cottageRepository;
        this.reservationService = reservationService;
        this.pictureService = pictureService;
        this.roomService = roomService;
        this.ruleOfConductService = ruleOfConductService;
        this.placeService = placeService;
        this.userService = userService;
    }

    public Cottage getCottageById(Long id) {
        return cottageRepository.getCottageById(id);
    }

    public Cottage getCottageByIdCanBeDeleted(Long id) {
        return cottageRepository.getCottageByIdCanBeDeleted(id);
    }

    public List<Cottage> getCottagesByOwnerId(Long id) {
        return cottageRepository.getCottagesByOwnerId(id);
    }

    public List<Cottage> findAll() { return cottageRepository.findAll();    }

    public List<Cottage> findAllByOwnerId(Long id) {
        List<Cottage> all = findAll();
        List<Cottage> retVal = new ArrayList<>();
        for (Cottage c : all) {
            if (c.getCottageOwner().getId().equals(id))
                retVal.add(c);
        }
        return retVal;
    }

    public String updateCottage(CottageDTO cottageDTO, Long id){
        try {
            if (reservationService.findAllActiveReservationsForEntityid(id).size() != 0)
                return "Cant update cottage because there exist active reservations";

            Cottage cottage = getCottageById(id);
            if (cottage == null) return "Cant find cottage with id " + id + ".";
            if (cottage.getVersion() != cottageDTO.getVersion()) return "Conflict seems to have occurred, someone changed your cottage data before you. Please refresh page and try again";
            cottage.setName(cottageDTO.getName());
            cottage.setAddress(cottageDTO.getAddress());
            cottage.setPromoDescription(cottageDTO.getPromoDescription());
            cottage.setEntityCancelationRate(cottageDTO.getEntityCancelationRate());
            cottage.setEntityType(EntityType.COTTAGE);

            Place p = cottageDTO.getPlace();
            if (p == null) return "Cant find chosen place.";
            Place place = placeService.getPlaceByZipCode(p.getZipCode());
            cottage.setPlace(place);

            if (cottageDTO.getRooms().isEmpty()) return "Cannot change cottage to be without room.";
            Cottage oldCottage = getCottageById(id);
            Set<Room> rooms = tryToEditCottageRooms(cottageDTO, oldCottage);
            cottage.setRooms(rooms);

            Set<RuleOfConduct> rules = new HashSet<RuleOfConduct>();

            tryToEditCottageRulesOfConduct(cottageDTO, oldCottage, rules);
            cottage.setRulesOfConduct(rules);
            pictureService.setNewImagesForBookingEntity(cottage, cottageDTO.getImages());
            cottage = save(cottage);
            return "";
        } catch (ObjectOptimisticLockingFailureException e) {
            return "Conflict seems to have occurred, someone changed your ship before you. Please refresh page and try again";
        }
    }

    @Transactional
    public String saveCottage(CottageDTO cottageDTO, Long idCottageOwner){
        Cottage cottage = new Cottage();
        cottage.setName(cottageDTO.getName());
        cottage.setAddress(cottageDTO.getAddress());
        cottage.setPromoDescription(cottageDTO.getPromoDescription());
        cottage.setEntityCancelationRate(cottageDTO.getEntityCancelationRate());

        cottage.setEntityType(EntityType.COTTAGE);
        if (cottageDTO.getPlace() == null) return "Cant find place.";
        Place place1 = placeService.getPlaceByZipCode(cottageDTO.getPlace().getZipCode());
        if (place1 == null) return "Cant find place with zip code: " + cottageDTO.getPlace().getZipCode();
        cottage.setPlace(place1);
        CottageOwner co = (CottageOwner) userService.findUserById(idCottageOwner);
        if (co == null) return "Cant find cottage owner with id: " + idCottageOwner;
        cottage.setCottageOwner(co);
        if (cottageDTO.getRooms().isEmpty()) return "Cannot create new cottage without room";
        cottage.setRooms(cottageDTO.getRooms());
        cottage.setRulesOfConduct(cottageDTO.getRulesOfConduct());
        if (!cottageDTO.getImages().isEmpty()) {
            Set<Picture> createdPictures = pictureService.createPicturesFromDTO(cottageDTO.getImages());
            cottage.setPictures(createdPictures);
        }
        cottage.setVersion(0);
        cottage.setLocked(false);
        cottage = save(cottage);

        return cottage.getId().toString();
    }

    public Cottage save(Cottage cottage) {
        return cottageRepository.save(cottage);
    }


    public Set<Room> tryToEditCottageRooms(CottageDTO cottageDTO, Cottage oldCottage) {
        Set<Room> rooms = new HashSet<Room>();

        for (Room oldRoom: oldCottage.getRooms()) {
            boolean found = false;
            for (Room room: cottageDTO.getRooms()){
                if (room.getRoomNum() == oldRoom.getRoomNum()){
                    found = true;
                    break;
                }
            }
            if (!found) { roomService.deleteById(oldRoom.getId()); }
            else{
                rooms.add(oldRoom);
            }
        }
        for (Room room: cottageDTO.getRooms()){
            boolean found = false;
            for (Room addedRoom: rooms){

                if (room.getRoomNum() == addedRoom.getRoomNum()){
                    found = true;
                    break;
                }
            }
            if (!found) { rooms.add(room); }
        }
        return rooms;
    }

    public void tryToEditCottageRulesOfConduct(CottageDTO cottageDTO, Cottage oldCottage, Set<RuleOfConduct> rules) {
        for (RuleOfConduct oldRule: oldCottage.getRulesOfConduct()) {
            boolean found = false;
            for (RuleOfConduct rule: cottageDTO.getRulesOfConduct()){
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
        for (RuleOfConduct rule: cottageDTO.getRulesOfConduct()){
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


    public User getCottageOwnerOfCottageId(Long id) {
        return this.cottageRepository.getCottageOwnerOfCottageId(id);
    }

    public Cottage findById(Long id) {
        Optional<Cottage> cottage = cottageRepository.findById(id);
        return cottage.orElse(null);
    }
}
