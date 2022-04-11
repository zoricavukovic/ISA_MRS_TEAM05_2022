package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.service.CottageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/cottages")
public class CottageController {

    private CottageService cottageService;

    @Autowired
    public CottageController(CottageService cottageService) {
        this.cottageService = cottageService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<CottageDTO> getCottageById(@PathVariable Long id) {
        Cottage cottage = cottageService.getCottageById(id);
        if (cottage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        CottageDTO cottageDTO = new CottageDTO(cottage);

        cottageDTO.setPlace(cottage.getPlace());
        cottageDTO.setRulesOfConduct(cottage.getRulesOfConduct());
        cottageDTO.setRooms(cottage.getRooms());
        return new ResponseEntity<>(cottageDTO, HttpStatus.OK);
    }

}


