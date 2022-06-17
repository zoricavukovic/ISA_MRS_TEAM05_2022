package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.exception.ApiRequestException;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.entities.CottageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@RestController
@RequestMapping("/cottages")
public class CottageController {

    private CottageService cottageService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public CottageController(CottageService cottageService, BookingEntityService bookingEntityService) {
        this.cottageService = cottageService;
        this.bookingEntityService = bookingEntityService;
    }

    public CottageController(){}

    @GetMapping
    public ResponseEntity<List<CottageDTO>> getCottages() {
        List<CottageDTO> cottageDTOs = cottageService.findAllCottageDTOs();
        return new ResponseEntity<>(cottageDTOs, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<CottageDTO> getCottageById(@PathVariable Long id) {
        CottageDTO cottageDTO = cottageService.getCottageDTOById(id);
        return new ResponseEntity<>(cottageDTO, HttpStatus.OK);
    }

    @GetMapping(value="/deleted/{id}")
    public ResponseEntity<CottageDTO> getCottageByIdCanBeDeleted(@PathVariable Long id) {
        CottageDTO cottageDTO = cottageService.getCottageDTOByIdCanBeDeleted(id);
        return new ResponseEntity<>(cottageDTO, HttpStatus.OK);
    }

    @GetMapping(value="/owner/{id}")
    public ResponseEntity<List<CottageDTO>> getCottageByOwnerId(@PathVariable Long id) {
        List<CottageDTO> cottageDTOS = cottageService.getCottagesDTOsByOwnerId(id);
        return new ResponseEntity<>(cottageDTOS, HttpStatus.OK);
    }

    @GetMapping(value="/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getCottagesForView() {
        List<Cottage> cottages = cottageService.findAll();
        return new ResponseEntity<>(getSearchBookingEntitiesFromCottages(cottages), HttpStatus.OK);
    }

    @GetMapping(value="/view/forOwnerId/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getCottagesForViewOwner(@PathVariable Long ownerId) {
        List<Cottage> cottages = cottageService.findAllByOwnerId(ownerId);
        return new ResponseEntity<>(getSearchBookingEntitiesFromCottages(cottages), HttpStatus.OK);
    }

    @GetMapping(value="/topRated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getTopRatedCottagesForView() {
        return new ResponseEntity<>(bookingEntityService.findTopRated("cottage"), HttpStatus.OK);
    }

    @PostMapping(value = "{idCottageOwner}", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public ResponseEntity<String> saveCottage(@PathVariable Long idCottageOwner, @Valid @RequestBody CottageDTO cottageDTO) {
        try{
            String retVal = cottageService.saveCottage(cottageDTO, idCottageOwner);
            return new ResponseEntity<>(retVal, HttpStatus.CREATED);
        }
        catch (ValidationException ex){
            throw new ApiRequestException("Someone is changing your values of new entity!");
        }
    }

    @PutMapping(value="/{id}", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public ResponseEntity<Object> updateCottage(@Valid @RequestBody CottageDTO cottageDTO, @PathVariable Long id) {
        cottageService.updateCottage(cottageDTO, id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping(value="/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getSearchedCottage(@RequestBody SearchParamsForEntity searchParams) {
        try {
            List<SearchedBookingEntityDTO> cottageDTOS = bookingEntityService.getSearchedBookingEntities(searchParams, "cottage");
            return new ResponseEntity<>(cottageDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    private List<SearchedBookingEntityDTO> getSearchBookingEntitiesFromCottages(List<Cottage> cottages) {
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Cottage c : cottages) {
            SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(c.getId());
            retVal.add(s);
        }
        return retVal;
    }
}
