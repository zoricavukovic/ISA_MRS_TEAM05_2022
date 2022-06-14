package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.SearchParamsForEntity;
import com.example.BookingAppTeam05.dto.entities.AdventureDTO;
import com.example.BookingAppTeam05.dto.users.InstructorDTO;
import com.example.BookingAppTeam05.dto.users.NewAdventureDTO;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.exception.ApiRequestException;
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
        return getAdventureDTOResponseEntity(adventure);
    }

    @GetMapping(value="/deleted/{id}")
    public ResponseEntity<AdventureDTO> getAdventureByIdCanBeDeleted(@PathVariable Long id) {
        Adventure adventure = adventureService.getAdventureByIdCanBeDeleted(id);
        return getAdventureDTOResponseEntity(adventure);
    }

    private ResponseEntity<AdventureDTO> getAdventureDTOResponseEntity(Adventure adventure) {
        if (adventure == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        AdventureDTO adventureDTO = new AdventureDTO(adventure);
        adventureDTO.setPlace(adventure.getPlace());
        adventureDTO.setRulesOfConduct((adventure.getRulesOfConduct()));
        adventureDTO.setFishingEquipment(adventure.getFishingEquipment());
        adventureDTO.setPictures(adventure.getPictures());
        adventureDTO.setInstructor(new InstructorDTO(adventure.getInstructor()));
        return new ResponseEntity<>(adventureDTO, HttpStatus.OK);
    }


    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createAdventure(@Valid @RequestBody NewAdventureDTO newAdventureDTO) {
        try{
            String retVal = adventureService.createAdventure(newAdventureDTO);
            Integer.parseInt(retVal);
            return new ResponseEntity<>(retVal, HttpStatus.CREATED);
        }
        catch (NumberFormatException ex){
            throw new ApiRequestException("Someone is changing your values of new entity!");
        }
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editAdventure(@RequestBody NewAdventureDTO newAdventureDTO, @PathVariable Long id) {

        String retVal = adventureService.updateAdventure(newAdventureDTO, id);
        if (retVal.isEmpty()) return new ResponseEntity<>(HttpStatus.OK);
        throw new ApiRequestException(retVal);

    }

    @GetMapping(value="/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getAdventuresForView() {
        List<Adventure> adventures = adventureService.findAll();
        return new ResponseEntity<>(getSearchedBookingEntitesFromAdventures(adventures), HttpStatus.OK);
    }

    private List<SearchedBookingEntityDTO> getSearchedBookingEntitesFromAdventures(List<Adventure> adventures) {
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Adventure adventure : adventures) {
            SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(adventure.getId());
            retVal.add(s);
        }
        return retVal;
    }


    @GetMapping(value="/view/forOwnerId/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getAdventuresForViewByOwnerId(@PathVariable Long ownerId) {
        List<Adventure> adventures = adventureService.findAllForOwnerId(ownerId);
        return new ResponseEntity<>(getSearchedBookingEntitesFromAdventures(adventures), HttpStatus.OK);
    }

    @GetMapping(value="/topRated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getTopRatedAdventuresForView() {
        List<SearchedBookingEntityDTO> retVal = bookingEntityService.findTopRated("adventure");
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
