package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/cottages")
public class CottageController {

    private CottageService cottageService;
    private PlaceService placeService;
    private UserService userService;
    private RoomService roomService;
    private RuleOfConductService ruleOfConductService;
    private PricelistService pricelistService;

    @Autowired
    public CottageController(CottageService cottageService, PlaceService placeService, UserService userService,
                             RoomService roomService, RuleOfConductService ruleOfConductService, PricelistService pricelistService) {
        this.cottageService = cottageService;
        this.placeService = placeService;
        this.userService = userService;
        this.roomService = roomService;
        this.ruleOfConductService = ruleOfConductService;
        this.pricelistService = pricelistService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<CottageDTO> getCottageById(@PathVariable Long id) {
        Cottage cottage = cottageService.getCottageById(id);
        if (cottage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        CottageDTO cottageDTO = new CottageDTO(cottage);

        cottageDTO.setPlace(cottage.getPlace());
        if (cottage.getRulesOfConduct() != null){
            cottageDTO.setRulesOfConduct(cottage.getRulesOfConduct());
        }
        if (cottage.getRooms() != null){
            cottageDTO.setRooms(cottage.getRooms());
        }
        return new ResponseEntity<>(cottageDTO, HttpStatus.OK);
    }
    @GetMapping(value="/owner/{id}")
    public ResponseEntity<List<CottageDTO>> getCottageByOwnerId(@PathVariable Long id) {
        List<Cottage> cottageFound = cottageService.getCottagesByOwnerId(id);
        List<CottageDTO> cottageDTOs = new ArrayList<>();

        for (Cottage cottage:cottageFound) {
            CottageDTO cDTO = new CottageDTO(cottage);
            cDTO.setPlace(cottage.getPlace());
            cottageDTOs.add(cDTO);
        }

        return ResponseEntity.ok(cottageDTOs);
    }

    @GetMapping
    public ResponseEntity<List<CottageDTO>> getCottages() {
        List<Cottage> cottages = cottageService.findAll();

        List<CottageDTO> cottageDTOs = new ArrayList<>();

        for (Cottage cottage:cottages) {
            CottageDTO cDTO = new CottageDTO(cottage);
            cDTO.setPlace(cottage.getPlace());
            cDTO.setRulesOfConduct(cottage.getRulesOfConduct());
            cDTO.setRooms(cottage.getRooms());
            cottageDTOs.add(cDTO);
        }

        return ResponseEntity.ok(cottageDTOs);
    }


    @PutMapping(consumes = "application/json")
    public ResponseEntity<CottageDTO> updateCottage(@RequestBody CottageDTO cottageDTO) {
        Cottage cottage = cottageService.getCottageById(cottageDTO.getId());
        if (cottage == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        cottage.setName(cottageDTO.getName());
        cottage.setAddress(cottageDTO.getAddress());
        cottage.setPromoDescription(cottageDTO.getPromoDescription());
        cottage.setEntityCancelationRate(cottageDTO.getEntityCancelationRate());
        cottage.setEntityType(EntityType.COTTAGE);
        Place place = placeService.getPlaceByZipCode(cottageDTO.getPlace().getZipCode());
        cottage.setPlace(place);

        Cottage oldCottage = cottageService.getCottageById(cottageDTO.getId());
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
        cottage.setRooms(rooms);

        Set<RuleOfConduct> rules = new HashSet<RuleOfConduct>();

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
        cottage.setRulesOfConduct(rules);

        cottage = cottageService.save(cottage);
        return new ResponseEntity<>(new CottageDTO(cottage), HttpStatus.OK);
    }
}
