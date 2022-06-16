package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.SearchParamsForEntity;
import com.example.BookingAppTeam05.dto.entities.AdventureDTO;
import com.example.BookingAppTeam05.dto.users.NewAdventureDTO;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.exception.ApiRequestException;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.service.entities.AdventureService;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/adventures")
public class AdventureController {

    private AdventureService adventureService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public AdventureController(AdventureService adventureService, BookingEntityService bookingEntityService) {
        this.adventureService = adventureService;
        this.bookingEntityService = bookingEntityService;
    }

    public AdventureController(){}

    @GetMapping(value="/{id}")
    public ResponseEntity<AdventureDTO> getAdventureById(@PathVariable Long id) {
        AdventureDTO adventureDTO = adventureService.getAdventureDTO(id);
        return new ResponseEntity<>(adventureDTO, HttpStatus.OK);
    }

    @GetMapping(value="/deleted/{id}")
    public ResponseEntity<AdventureDTO> getAdventureByIdCanBeDeleted(@PathVariable Long id) {
        AdventureDTO adventureDTO = adventureService.getAdventureDTOByIdCanBeDeleted(id);
        return new ResponseEntity<>(adventureDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createAdventure(@Valid @RequestBody NewAdventureDTO newAdventureDTO) {
        try{
            String retVal = adventureService.createAdventure(newAdventureDTO);
            return new ResponseEntity<>(retVal, HttpStatus.CREATED);
        }
        catch (NumberFormatException ex){
            throw new ApiRequestException("Someone is changing your values of new entity!");
        }
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editAdventure(@RequestBody NewAdventureDTO newAdventureDTO, @PathVariable Long id) {
        adventureService.updateAdventure(newAdventureDTO, id);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping(value="/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getAdventuresForView() {
        List<Adventure> adventures = adventureService.findAll();
        return new ResponseEntity<>(getSearchedBookingEntitiesFromAdventures(adventures), HttpStatus.OK);
    }

    @GetMapping(value="/view/forOwnerId/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getAdventuresForViewByOwnerId(@PathVariable Long ownerId) {
        List<Adventure> adventures = adventureService.findAllForOwnerId(ownerId);
        return new ResponseEntity<>(getSearchedBookingEntitiesFromAdventures(adventures), HttpStatus.OK);
    }

    @GetMapping(value="/topRated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getTopRatedAdventuresForView() {
        return new ResponseEntity<>(bookingEntityService.findTopRated("adventure"), HttpStatus.OK);
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

    private List<SearchedBookingEntityDTO> getSearchedBookingEntitiesFromAdventures(List<Adventure> adventures) {
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Adventure adventure : adventures) {
            SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(adventure.getId());
            retVal.add(s);
        }
        return retVal;
    }
}
