package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.service.CottageService;
import com.example.BookingAppTeam05.service.PricelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/pricelists")
public class PricelistController {

    private PricelistService pricelistService;

    @Autowired
    public PricelistController(PricelistService pricelistService) {
        this.pricelistService = pricelistService;
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

}
