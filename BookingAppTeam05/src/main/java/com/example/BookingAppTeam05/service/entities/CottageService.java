package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.RuleOfConduct;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.repository.entities.CottageRepository;
import com.example.BookingAppTeam05.service.PictureService;
import com.example.BookingAppTeam05.service.ReservationService;
import com.example.BookingAppTeam05.service.RoomService;
import com.example.BookingAppTeam05.service.RuleOfConductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    public List<Cottage> findAll() { return cottageRepository.findAll();    }

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