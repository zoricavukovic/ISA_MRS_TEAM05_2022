package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.users.CottageOwner;
import com.example.BookingAppTeam05.service.*;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/cottages")
@CrossOrigin
public class CottageController {

    private CottageService cottageService;
    private PlaceService placeService;
    private UserService userService;
    private RoomService roomService;
    private RuleOfConductService ruleOfConductService;
    private PricelistService pricelistService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public CottageController(CottageService cottageService, PlaceService placeService, UserService userService,
                             RoomService roomService, RuleOfConductService ruleOfConductService, PricelistService pricelistService, BookingEntityService bookingEntityService) {
        this.cottageService = cottageService;
        this.placeService = placeService;
        this.userService = userService;
        this.roomService = roomService;
        this.ruleOfConductService = ruleOfConductService;
        this.pricelistService = pricelistService;
        this.bookingEntityService = bookingEntityService;
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
        cottageDTO.setPictures(cottage.getPictures());
        return new ResponseEntity<>(cottageDTO, HttpStatus.OK);
    }
    @GetMapping(value="/editQue/{cottageId}")
    public ResponseEntity<String> checkIfCanEdit(@PathVariable Long cottageId) {
        Cottage cottage = cottageService.findCottageByCottageIdWithOwner(cottageId);
        if (cottage == null) return new ResponseEntity<String>("Cottage for editing is not found.", HttpStatus.BAD_REQUEST);
        if (cottageService.checkExistActiveReservations(cottageId)){
            return new ResponseEntity<String>("Cannot edit cottage cause has reservations.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Cottage can edit.", HttpStatus.OK);
    }

    @GetMapping(value="/owner/{id}")
    public ResponseEntity<List<CottageDTO>> getCottageByOwnerId(@PathVariable Long id) {
        List<Cottage> cottageFound = cottageService.getCottagesByOwnerId(id);
        List<CottageDTO> cottageDTOs = new ArrayList<>();

        for (Cottage cottage:cottageFound) {
            CottageDTO cDTO = new CottageDTO(cottage);
            cDTO.setPlace(cottage.getPlace());
            cDTO.setPictures(cottage.getPictures());
            cottageDTOs.add(cDTO);
        }

        return ResponseEntity.ok(cottageDTOs);
    }


    @GetMapping(value="/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getCottagesForView() {
        List<Cottage> cottages = cottageService.findAll();
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Cottage c : cottages) {
            SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(c.getId());
            retVal.add(s);
        }
        return new ResponseEntity<>(retVal, HttpStatus.OK);
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
            cDTO.setPictures(cottage.getPictures());
            cottageDTOs.add(cDTO);
        }

        return ResponseEntity.ok(cottageDTOs);
    }

    @PostMapping(value = "{idCottageOwner}", consumes = "application/json")
    @Transactional
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public ResponseEntity<String> saveCottage(@PathVariable Long idCottageOwner, @RequestBody CottageDTO cottageDTO) {

        Cottage cottage = new Cottage();
        cottage.setName(cottageDTO.getName());
        cottage.setAddress(cottageDTO.getAddress());
        cottage.setPromoDescription(cottageDTO.getPromoDescription());
        cottage.setEntityCancelationRate(cottageDTO.getEntityCancelationRate());

        cottage.setEntityType(EntityType.COTTAGE);
        Place place1 = placeService.getPlaceByZipCode(cottageDTO.getPlace().getZipCode());
        if (place1 == null) return new ResponseEntity<>("Cant find place with zip code: " + cottageDTO.getPlace().getZipCode(), HttpStatus.BAD_REQUEST);
        cottage.setPlace(place1);
        CottageOwner co = (CottageOwner) userService.getUserById(idCottageOwner);
        if (co == null) return new ResponseEntity<>("Cant find cottage owner with id: " + idCottageOwner, HttpStatus.BAD_REQUEST);
        cottage.setCottageOwner(co);
        cottage.setRooms(cottageDTO.getRooms());
        cottage.setRulesOfConduct(cottageDTO.getRulesOfConduct());
//        if (!cottageDTO.getImages().isEmpty()) {
//            Set<Picture> createdPictures = pictureService.createPicturesFromDTO(newAdventureDTO.getImages());
//            adventure.setPictures(createdPictures);
//        }
        cottage = cottageService.save(cottage);

        return new ResponseEntity<>(cottage.getId().toString(), HttpStatus.CREATED);
    }

    @PutMapping(value="/{id}", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public ResponseEntity<CottageDTO> updateCottage(@RequestBody CottageDTO cottageDTO, @PathVariable Long id) {
        Cottage cottage = cottageService.getCottageById(id);
        if (cottage == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        cottage.setName(cottageDTO.getName());
        cottage.setAddress(cottageDTO.getAddress());
        cottage.setPromoDescription(cottageDTO.getPromoDescription());
        cottage.setEntityCancelationRate(cottageDTO.getEntityCancelationRate());
        cottage.setEntityType(EntityType.COTTAGE);
        Place place = placeService.getPlaceByZipCode(cottageDTO.getPlace().getZipCode());
        cottage.setPlace(place);
        Cottage oldCottage = cottageService.getCottageById(id);
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
        System.out.println("Veli " + cottageDTO.getRooms().size());
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
        System.out.println("Stigao dovde 44");
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
        cottageService.setNewImages(cottage, cottage.getPictures());
        cottage = cottageService.save(cottage);
        return new ResponseEntity<>(new CottageDTO(cottage), HttpStatus.OK);
    }

    @DeleteMapping(value="/{cottageId}/{confirmPass}")
    @Transactional
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public ResponseEntity<String> logicalDeleteCottageById(@PathVariable Long cottageId, @PathVariable String confirmPass){
        Cottage cottage = cottageService.findCottageByCottageIdWithOwner(cottageId);
        if (cottage == null) return new ResponseEntity<String>("Cottage for deleting is not found.", HttpStatus.BAD_REQUEST);
        if (!cottage.getCottageOwner().getPassword().equals(confirmPass)) return new ResponseEntity<String>("Confirmation password is incorrect.", HttpStatus.BAD_REQUEST);
        if (!cottageService.logicalDeleteCottageById(cottageId)){
            return new ResponseEntity<String>("Cottage is not delete cause has reservations.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Cottage is deleted.", HttpStatus.CREATED);
    }

    @GetMapping(value="/{ownerId}/search/{cottageName}/{city}/{rate}/{cost}/{firstOp}/{secondOp}/{thirdOp}")
    public ResponseEntity<List<CottageDTO>> searchCottages(@PathVariable Long ownerId, @PathVariable String cottageName, @PathVariable String city,
                                                           @PathVariable float rate,  @PathVariable double cost, @PathVariable String firstOp,
                                                           @PathVariable String secondOp, @PathVariable String thirdOp) {
        System.out.println(cottageName + " ime i adresa " + city + " " + rate + " " + firstOp + " " + secondOp + " " + thirdOp);
        List<Cottage> firstList = cottageService.searchCottagesOfOwner(ownerId, cottageName, firstOp, city, secondOp, rate);

        List<Cottage> cottages = new ArrayList<Cottage>();
        for (Cottage cottage: firstList){
            List<Pricelist> pricelistList =  pricelistService.getCurrentPricelistByBookingEntityId(cottage.getId());
            if (pricelistList == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            Pricelist p = pricelistList.get(0);

            if (thirdOp.equals("AND") && cost != 0){
                if (p.getEntityPricePerPerson() >= cost){
                    cottages.add(cottage);
                }
            }else{
                cottages.add(cottage);
            }
        }
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


}
