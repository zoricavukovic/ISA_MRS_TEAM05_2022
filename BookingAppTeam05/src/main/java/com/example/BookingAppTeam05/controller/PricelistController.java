package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pricelists")
public class PricelistController {

    private PricelistService pricelistService;

    @Autowired
    public PricelistController(PricelistService pricelistService) {
        this.pricelistService = pricelistService;
    }

    public PricelistController(){}

    @GetMapping(value="/{id}")
    public ResponseEntity<PricelistDTO> getById(@PathVariable Long id) {
        PricelistDTO pricelistDTO = pricelistService.getCurrentPricelistDTOByBookingEntityId(id);
        return new ResponseEntity<>(pricelistDTO, HttpStatus.OK);
    }

    @PostMapping(value="/{idBookingEntity}", consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<PricelistDTO> updatePricelist(@PathVariable Long idBookingEntity, @RequestBody PricelistDTO pricelistDTO)  {
        Pricelist pricelist = pricelistService.updatePricelist(idBookingEntity, pricelistDTO);
        return new ResponseEntity<>(new PricelistDTO(pricelist), HttpStatus.CREATED);
    }
}
