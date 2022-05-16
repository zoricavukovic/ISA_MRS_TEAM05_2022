package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.SearchParamsForEntity;
import com.example.BookingAppTeam05.dto.entities.AdventureDTO;
import com.example.BookingAppTeam05.dto.entities.NewAdventureDTO;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.users.Instructor;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.service.entities.AdventureService;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.users.InstructorService;
import com.example.BookingAppTeam05.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/adventures")
public class AdventureController {

    private AdventureService adventureService;
    private PlaceService placeService;
    private InstructorService instructorService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public AdventureController(AdventureService adventureService, PlaceService placeService, InstructorService instructorService, BookingEntityService bookingEntityService) {
        this.placeService = placeService;
        this.adventureService = adventureService;
        this.instructorService = instructorService;
        this.bookingEntityService = bookingEntityService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<AdventureDTO> getAdventureById(@PathVariable Long id) {
        Adventure adventure = adventureService.getAdventureById(id);
        if (adventure == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        AdventureDTO adventureDTO = new AdventureDTO(adventure);
        adventureDTO.setPlace(adventure.getPlace());
        adventureDTO.setRulesOfConduct((adventure.getRulesOfConduct()));
        adventureDTO.setFishingEquipment(adventure.getFishingEquipment());
        adventureDTO.setPictures(adventure.getPictures());
        return new ResponseEntity<>(adventureDTO, HttpStatus.OK);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createAdventure(@Valid @RequestBody NewAdventureDTO newAdventureDTO) {
        Place place = placeService.getPlaceById(newAdventureDTO.getPlaceId());
        if (place == null) {
            return new ResponseEntity<>("Cant find place with id: " + newAdventureDTO.getPlaceId(), HttpStatus.BAD_REQUEST);
        }
        Instructor instructor = instructorService.findById(newAdventureDTO.getInstructorId());
        if (instructor == null) {
            return new ResponseEntity<>("Cant find instructor with id: " + newAdventureDTO.getInstructorId(), HttpStatus.BAD_REQUEST);
        }
        Adventure newAdventure = adventureService.createNewAdventure(newAdventureDTO, place, instructor);
        if (newAdventure == null) {
            return new ResponseEntity<>("Error. Cant create adventure", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newAdventure.getId().toString(), HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editAdventure(@RequestBody NewAdventureDTO newAdventureDTO, @PathVariable Long id) {
        if (bookingEntityService.checkExistActiveReservationForEntityId(id))
            return new ResponseEntity<>("Cant update adventure because there exist active reservations", HttpStatus.BAD_REQUEST);

        Place place = placeService.getPlaceById(newAdventureDTO.getPlaceId());
        if (place == null) {
            return new ResponseEntity<>("Cant find place with id: " + newAdventureDTO.getPlaceId(), HttpStatus.BAD_REQUEST);
        }
        Instructor instructor = instructorService.findById(newAdventureDTO.getInstructorId());
        if (instructor == null) {
            return new ResponseEntity<>("Cant find instructor with id: " + newAdventureDTO.getInstructorId(), HttpStatus.BAD_REQUEST);
        }
        Adventure adventure = adventureService.getAdventureById(id);
        if (adventure == null) {
            return new ResponseEntity<>("Cant find adventure with id: " + id, HttpStatus.BAD_REQUEST);
        }
        Adventure updated = adventureService.editAdventureById(id, newAdventureDTO, place);
        return new ResponseEntity<>("entity changed", HttpStatus.OK);
    }

    @GetMapping(value="/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getAdventuresForView() {
        List<Adventure> adventures = adventureService.findAll();
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Adventure adventure : adventures) {
            SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(adventure.getId());
            retVal.add(s);
        }
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }


    @PostMapping(value="/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getSearchedAdventures(@RequestBody SearchParamsForEntity searchParams) {
        try {
            List<SearchedBookingEntityDTO> adventureDTOS = bookingEntityService.getSearchedBookingEntities(searchParams, "instructor");
            return new ResponseEntity<>(adventureDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
