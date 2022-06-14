package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.dto.users.CottageOwnerDTO;
import com.example.BookingAppTeam05.exception.ApiRequestException;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.users.CottageOwner;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.entities.CottageService;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cottages")
@CrossOrigin
public class CottageController {

    private CottageService cottageService;
    private PlaceService placeService;
    private UserService userService;
    private PricelistService pricelistService;
    private BookingEntityService bookingEntityService;
    private PictureService pictureService;
    private SearchService searchService;

    @Autowired
    public CottageController(CottageService cottageService, PlaceService placeService, UserService userService, PricelistService pricelistService,
                             BookingEntityService bookingEntityService, PictureService pictureService, SearchService searchService) {
        this.cottageService = cottageService;
        this.placeService = placeService;
        this.userService = userService;
        this.pricelistService = pricelistService;
        this.bookingEntityService = bookingEntityService;
        this.pictureService = pictureService;
        this.searchService = searchService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<CottageDTO> getCottageById(@PathVariable Long id) {
        Cottage cottage = cottageService.getCottageById(id);
        return getCottageDTOResponseEntity(cottage);
    }

    @GetMapping(value="/deleted/{id}")
    public ResponseEntity<CottageDTO> getCottageByIdCanBeDeleted(@PathVariable Long id) {
        Cottage cottage = cottageService.getCottageByIdCanBeDeleted(id);
        return getCottageDTOResponseEntity(cottage);
    }

    private ResponseEntity<CottageDTO> getCottageDTOResponseEntity(Cottage cottage) {
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
        if(cottage.getCottageOwner() != null){
            cottageDTO.setCottageOwnerDTO(new CottageOwnerDTO(cottage.getCottageOwner()));
        }

        cottageDTO.setPictures(cottage.getPictures());
        return new ResponseEntity<>(cottageDTO, HttpStatus.OK);
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
        return new ResponseEntity<>(getSearchBookingEntitiesFromCottages(cottages), HttpStatus.OK);
    }

    private List<SearchedBookingEntityDTO> getSearchBookingEntitiesFromCottages(List<Cottage> cottages) {
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Cottage c : cottages) {
            SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(c.getId());
            retVal.add(s);
        }
        return retVal;
    }

    @GetMapping(value="/view/forOwnerId/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getCottagesForView(@PathVariable Long ownerId) {
        List<Cottage> cottages = cottageService.findAllByOwnerId(ownerId);
        return new ResponseEntity<>(getSearchBookingEntitiesFromCottages(cottages), HttpStatus.OK);
    }


    @GetMapping(value="/topRated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getTopRatedCottagesForView() {
        List<SearchedBookingEntityDTO> retVal = bookingEntityService.findTopRated("cottage");
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
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public ResponseEntity<String> saveCottage(@PathVariable Long idCottageOwner,@Valid @RequestBody CottageDTO cottageDTO) {
        try{
            String retVal = cottageService.saveCottage(cottageDTO, idCottageOwner);
            Integer.parseInt(retVal);
            return new ResponseEntity<>(retVal, HttpStatus.CREATED);
        }
        catch (NumberFormatException ex){
            throw new ApiRequestException("Someone is changing your values of new entity!");
        }

    }

    @PutMapping(value="/{id}", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public ResponseEntity<Object> updateCottage(@Valid @RequestBody CottageDTO cottageDTO, @PathVariable Long id) {
        String retVal = cottageService.updateCottage(cottageDTO, id);
        if (retVal.isEmpty()) return new ResponseEntity<>(HttpStatus.OK);
        throw new ApiRequestException(retVal);

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
}
