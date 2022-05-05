package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.CottageDTO;
import com.example.BookingAppTeam05.dto.NewImageDTO;
import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.RuleOfConduct;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.repository.CottageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CottageService {
    private CottageRepository cottageRepository;
    private ReservationService reservationService;
    private PictureService pictureService;
    private RoomService roomService;
    private RuleOfConductService ruleOfConductService;

    @Autowired
    public CottageService(CottageRepository cottageRepository, ReservationService reservationService,
                          PictureService pictureService, RoomService roomService, RuleOfConductService ruleOfConductService) {
        this.cottageRepository = cottageRepository;
        this.reservationService = reservationService;
        this.pictureService = pictureService;
        this.roomService = roomService;
        this.ruleOfConductService = ruleOfConductService;
    }

    public Cottage getCottageById(Long id) {
        return cottageRepository.getCottageById(id);
    }
    public List<Cottage> getCottagesByOwnerId(Long id) {
        return cottageRepository.getCottagesByOwnerId(id);
    }
    
    public boolean logicalDeleteCottageById(Long id){
        if (checkExistActiveReservations(id)) return false;
        cottageRepository.logicalDeleteById(id);
        return true;
    }
    public boolean checkExistActiveReservations(Long id) {
        List<Reservation> reservations = reservationService.findAllActiveReservationsForEntity(id);
        for (Reservation r : reservations){
           if ((r.getStartDate().plusDays(r.getNumOfDays())).isAfter(LocalDateTime.now())) return true;
        }
        return false;
    }

    public List<Cottage> findAll() { return cottageRepository.findAll();    }

    //public Cottage getCottageRoomWithNum(int roomNum, Long cottageId){ return cottageRepository.getCottageRoomByNum(roomNum, cottageId);}

    public Cottage save(Cottage cottage) {
        return cottageRepository.save(cottage);
    }

    public Cottage findCottageByCottageIdWithOwner(Long cottageId) { return cottageRepository.findCottageByCottageIdWithOwner(cottageId);}

    public void setNewImages(Cottage existingCottage, List<NewImageDTO> images) {
        Set<Picture> pictures = new HashSet<>();

        for (Picture currentPicture : existingCottage.getPictures()) {
            boolean found = false;
            for (NewImageDTO newImage : images) {
                if (newImage.getImageName().equals(currentPicture.getPicturePath())) {
                    found = true;
                    pictures.add(currentPicture);
                    break;
                }
            }
            if (!found) {
                pictureService.deletePictureByName(currentPicture.getPicturePath());
            }
        }
        for (NewImageDTO newImage : images) {
            boolean found = false;
            for (Picture picture : pictures) {
                if (picture.getPicturePath().equals(newImage.getImageName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                pictureService.tryToSaveNewPictureAndAddToOtherPictures(pictures, newImage);
            }
        }
        existingCottage.setPictures(pictures);
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
