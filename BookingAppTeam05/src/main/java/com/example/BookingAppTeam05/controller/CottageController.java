package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.service.CottageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<CottageDTO>> getCottages() {
        List<Cottage> cottages = cottageService.findAll();

        List<CottageDTO> cottageDTOs = new ArrayList<>();

        for (Cottage cottage:cottages) {
            CottageDTO cDTO = new CottageDTO(cottage);
            cDTO.setPlace(cottage.getPlace());
            cDTO.setRulesOfConduct(cottage.getRulesOfConduct());
            cottageDTOs.add(cDTO);
        }

        return ResponseEntity.ok(cottageDTOs);
    }

}


