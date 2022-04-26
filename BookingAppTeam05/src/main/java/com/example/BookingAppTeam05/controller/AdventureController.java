package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.AdventureDTO;
import com.example.BookingAppTeam05.dto.NewAdventureDTO;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.users.Instructor;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.service.AdventureService;
import com.example.BookingAppTeam05.service.InstructorService;
import com.example.BookingAppTeam05.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;


@RestController
@RequestMapping("/adventures")
public class AdventureController {

    private AdventureService adventureService;
    private PlaceService placeService;
    private InstructorService instructorService;

    @Autowired
    public AdventureController(AdventureService adventureService, PlaceService placeService, InstructorService instructorService) {
        this.placeService = placeService;
        this.adventureService = adventureService;
        this.instructorService = instructorService;
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
}
