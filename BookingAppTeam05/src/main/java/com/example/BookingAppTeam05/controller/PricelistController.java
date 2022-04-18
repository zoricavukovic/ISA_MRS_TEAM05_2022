package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.service.AdditionalServiceService;
import com.example.BookingAppTeam05.service.CottageService;
import com.example.BookingAppTeam05.service.PricelistService;
import com.example.BookingAppTeam05.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/pricelists")
public class PricelistController {

    private PricelistService pricelistService;
    private CottageService cottageService;
    private AdditionalServiceService additionalServiceService;

    @Autowired
    public PricelistController(PricelistService pricelistService, CottageService cottageService, AdditionalServiceService additionalServiceService) {
        this.pricelistService = pricelistService;
        this.cottageService = cottageService;
        this.additionalServiceService = additionalServiceService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<PricelistDTO> getAllById(@PathVariable Long id) {
        List<Pricelist> pricelistList = pricelistService.getCurrentPricelistByBookingEntityId(id);
        if (pricelistList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Pricelist p = pricelistList.get(0);
        PricelistDTO pricelistDTO = new PricelistDTO(p);
        pricelistDTO.setAdditionalServices(p.getAdditionalServices());
        return new ResponseEntity<>(pricelistDTO, HttpStatus.OK);
    }

    @PostMapping(value="/{idCottage}", consumes = "application/json")
    public ResponseEntity<PricelistDTO> updatePricelist(@PathVariable Long idCottage, @RequestBody PricelistDTO pricelistDTO)  {
        Pricelist pricelist = new Pricelist();
        pricelist.setEntityPricePerPerson(pricelistDTO.getEntityPricePerPerson());

        pricelist.setBookingEntity(cottageService.getCottageById(idCottage));
        pricelist.setStartDate(LocalDateTime.now());
        pricelist.setAdditionalServices(pricelistDTO.getAdditionalServices());
        pricelist = pricelistService.save(pricelist);

        return new ResponseEntity<>(new PricelistDTO(pricelist), HttpStatus.CREATED);
    }

}
