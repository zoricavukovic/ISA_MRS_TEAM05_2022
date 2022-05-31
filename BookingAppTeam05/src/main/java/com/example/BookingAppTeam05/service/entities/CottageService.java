package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.RuleOfConduct;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.model.repository.entities.CottageRepository;
import com.example.BookingAppTeam05.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CottageService {
    private CottageRepository cottageRepository;
    private ReservationService reservationService;
    private PictureService pictureService;
    private RoomService roomService;
    private RuleOfConductService ruleOfConductService;
    private SearchService searchService;

    @Autowired
    public CottageService(CottageRepository cottageRepository, ReservationService reservationService,
                          PictureService pictureService, RoomService roomService, RuleOfConductService ruleOfConductService
            , SearchService searchService) {
        this.cottageRepository = cottageRepository;
        this.reservationService = reservationService;
        this.pictureService = pictureService;
        this.roomService = roomService;
        this.ruleOfConductService = ruleOfConductService;
        this.searchService = searchService;
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
