package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.dto.users.CottageOwnerDTO;
import com.example.BookingAppTeam05.exception.ConflictException;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.exception.database.CreateItemException;
import com.example.BookingAppTeam05.exception.database.EditItemException;
import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.RuleOfConduct;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.users.CottageOwner;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.repository.entities.CottageRepository;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public CottageService(){}

    public Cottage getCottageById(Long id) {
        return cottageRepository.getCottageById(id);
    }

    public CottageDTO getCottageDTOById(Long id) {
        Cottage cottage = getCottageById(id);
        if (cottage == null)
            throw new ItemNotFoundException("Can't find cottage with id: " + id + ". Refresh page and try again!");
        return getCottageDTO(cottage);
    }

    public Cottage getCottageByIdCanBeDeleted(Long id) {
        return cottageRepository.getCottageByIdCanBeDeleted(id);
    }

    public CottageDTO getCottageDTOByIdCanBeDeleted(Long id) {
        Cottage cottage = getCottageByIdCanBeDeleted(id);
        if (cottage == null)
            throw new ItemNotFoundException("Can't find cottage with id: " + id + ". Refresh page and try again!");
        return getCottageDTO(cottage);
    }

    private CottageDTO getCottageDTO(Cottage cottage) {
        CottageDTO cottageDTO = new CottageDTO(cottage);

        cottageDTO.setPlace(cottage.getPlace());
        if (cottage.getRulesOfConduct() != null){
            cottageDTO.setRulesOfConduct(cottage.getRulesOfConduct());
        }
        if (cottage.getRooms() != null){
            cottageDTO.setRooms(cottage.getRooms());
        }
        if(cottage.getCottageOwner() != null){
            cottageDTO.setCottageOwnerDTO(new CottageOwnerDTO(cottage.getCottageOwner()));
        }

        cottageDTO.setPictures(cottage.getPictures());
        return cottageDTO;
    }

    public List<Cottage> getCottagesByOwnerId(Long id) {
        return cottageRepository.getCottagesByOwnerId(id);
    }

    public List<CottageDTO> getCottagesDTOsByOwnerId(Long id) {
        List<Cottage> cottageFound = getCottagesByOwnerId(id);
        List<CottageDTO> cottageDTOs = new ArrayList<>();

        for (Cottage cottage:cottageFound) {
            CottageDTO cDTO = new CottageDTO(cottage);
            cDTO.setPlace(cottage.getPlace());
            cDTO.setPictures(cottage.getPictures());
            cottageDTOs.add(cDTO);
        }
        return cottageDTOs;
    }

    public List<Cottage> findAll() { return cottageRepository.findAll(); }

    public List<Cottage> findAllByOwnerId(Long id) {
        List<Cottage> all = findAll();
        List<Cottage> retVal = new ArrayList<>();
        for (Cottage c : all) {
            if (c.getCottageOwner().getId().equals(id))
                retVal.add(c);
        }
        return retVal;
    }

    public List<CottageDTO> findAllCottageDTOs(){
        List<Cottage> cottages = findAll();
        List<CottageDTO> cottageDTOs = new ArrayList<>();
        for (Cottage cottage:cottages) {
            CottageDTO cDTO = new CottageDTO(cottage);
            cDTO.setPlace(cottage.getPlace());
            cDTO.setRulesOfConduct(cottage.getRulesOfConduct());
            cDTO.setRooms(cottage.getRooms());
            cDTO.setPictures(cottage.getPictures());
            cottageDTOs.add(cDTO);
        }
        return cottageDTOs;
    }

    public User getCottageOwnerOfCottageId(Long id) {
        return this.cottageRepository.getCottageOwnerOfCottageId(id);
    }

    public Cottage findById(Long id) {
        Optional<Cottage> cottage = cottageRepository.findById(id);
        return cottage.orElse(null);
    }



    @Transactional
    public void updateCottage(CottageDTO cottageDTO, Long id){
        try {
            if (reservationService.findAllActiveReservationsForEntityid(id).size() != 0)
                throw new EditItemException("Can't update cottage because there exist active reservations");
            Cottage cottage = getCottageById(id);
            if (cottage == null)
                throw new ItemNotFoundException("Cant find cottage with id: " + id+ ". Refresh page and try again!");
            if (cottage.getVersion() != cottageDTO.getVersion())
                throw new ConflictException("Conflict seems to have occurred, someone changed your cottage data before you. Please refresh page and try again!");

            cottage.setName(cottageDTO.getName());
            cottage.setAddress(cottageDTO.getAddress());
            cottage.setPromoDescription(cottageDTO.getPromoDescription());
            cottage.setEntityCancelationRate(cottageDTO.getEntityCancelationRate());
            cottage.setEntityType(EntityType.COTTAGE);

            Place p = cottageDTO.getPlace();
            if (p == null)
                throw new ItemNotFoundException("Can't find chosen place.");
            Place place = placeService.getPlaceByZipCode(p.getZipCode());
            cottage.setPlace(place);

            if (cottageDTO.getRooms().isEmpty())
                throw new ItemNotFoundException("Can't change cottage to be without room.");
            Cottage oldCottage = getCottageById(id);
            Set<Room> rooms = tryToEditCottageRooms(cottageDTO, oldCottage);
            cottage.setRooms(rooms);

            Set<RuleOfConduct> rules = new HashSet<>();
            tryToEditCottageRulesOfConduct(cottageDTO, oldCottage, rules);
            cottage.setRulesOfConduct(rules);

            pictureService.setNewImagesForBookingEntity(cottage, cottageDTO.getImages());
            save(cottage);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConflictException("Conflict seems to have occurred, someone changed your cottage before you. Please refresh page and try again!");
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
        if (cottageDTO.getPlace() == null)
            throw new ItemNotFoundException("Can't create cottage without set place.");
        Place place1 = placeService.getPlaceByZipCode(cottageDTO.getPlace().getZipCode());
        if (place1 == null)
            throw new ItemNotFoundException("Can't find place with zip code: " + cottageDTO.getPlace().getZipCode());
        cottage.setPlace(place1);
        CottageOwner co = (CottageOwner) userService.findUserById(idCottageOwner);
        if (co == null)
            throw new ItemNotFoundException("Can't find cottage owner with id: " + idCottageOwner);
        cottage.setCottageOwner(co);
        if (cottageDTO.getRooms().isEmpty())
            throw new CreateItemException("Can't create new cottage without room.");
        cottage.setRooms(cottageDTO.getRooms());
        cottage.setRulesOfConduct(cottageDTO.getRulesOfConduct());
        if (!cottageDTO.getImages().isEmpty()) {
            Set<Picture> createdPictures = pictureService.createPicturesFromDTO(cottageDTO.getImages());
            cottage.setPictures(createdPictures);
        }
        cottage.setVersion(0);
        cottage.setLocked(false);
        cottage = save(cottage);
        if (cottage == null) throw new CreateItemException("Can't create new cottage. Try again!");
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


}
