package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.entities.CottageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/pricelists")
public class PricelistController {

    private PricelistService pricelistService;
    private CottageService cottageService;
    private AdditionalServiceService additionalServiceService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public PricelistController(PricelistService pricelistService, CottageService cottageService, BookingEntityService bookingEntityService, AdditionalServiceService additionalServiceService) {
        this.pricelistService = pricelistService;
        this.cottageService = cottageService;
        this.additionalServiceService = additionalServiceService;
        this.bookingEntityService = bookingEntityService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<PricelistDTO> getById(@PathVariable Long id) {
        List<Pricelist> pricelistList = pricelistService.getCurrentPricelistByBookingEntityId(id);
        if (pricelistList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Pricelist p = pricelistList.get(0);
        PricelistDTO pricelistDTO = new PricelistDTO(p);
        Set<NewAdditionalServiceDTO> newAs = new HashSet<>();
        for(AdditionalService as:p.getAdditionalServices())
            newAs.add(new NewAdditionalServiceDTO(as));
        pricelistDTO.setAdditionalServices(newAs);
        return new ResponseEntity<>(pricelistDTO, HttpStatus.OK);
    }

    @PostMapping(value="/{idBookingEntity}", consumes = "application/json")
    //@PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER','ROLE_SHIP_OWNER')")
    public ResponseEntity<PricelistDTO> updatePricelist(@PathVariable Long idBookingEntity, @RequestBody PricelistDTO pricelistDTO)  {
        Pricelist pricelist = new Pricelist();
        pricelist.setEntityPricePerPerson(pricelistDTO.getEntityPricePerPerson());

        pricelist.setBookingEntity(bookingEntityService.getBookingEntityById(idBookingEntity));
        pricelist.setStartDate(LocalDateTime.now());
        Set<AdditionalService> additionalServices= new HashSet<>();
        for(NewAdditionalServiceDTO newAs: pricelistDTO.getAdditionalServices())
        {
            AdditionalService as = new AdditionalService();
            as.setPrice(newAs.getPrice());
            as.setServiceName(newAs.getServiceName());
            as.setId(newAs.getId());
            additionalServices.add(as);
        }
        pricelist.setAdditionalServices(additionalServices);
        pricelist = pricelistService.save(pricelist);

        return new ResponseEntity<>(new PricelistDTO(pricelist), HttpStatus.CREATED);
    }

}
