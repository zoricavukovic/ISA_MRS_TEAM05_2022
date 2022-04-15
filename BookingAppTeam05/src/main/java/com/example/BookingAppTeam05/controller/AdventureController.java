package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.AdventureDTO;
import com.example.BookingAppTeam05.dto.CottageDTO;
import com.example.BookingAppTeam05.model.Adventure;
import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.service.AdventureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/adventures")
public class AdventureController {

    private AdventureService adventureService;

    @Autowired
    public AdventureController(AdventureService adventureService) {
        this.adventureService = adventureService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<AdventureDTO> getAdventureById(@PathVariable Long id) {
        Adventure adventure = adventureService.getAdventureById(id);
        if (adventure == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        AdventureDTO adventureDTO = new AdventureDTO(adventure);
        adventureDTO.setPlace(adventure.getPlace());
        adventureDTO.setRulesOfConduct((adventure.getRulesOfConduct()));
        adventureDTO.setFishingEquipment(adventure.getFishingEquipment());
        adventureDTO.setPictures(adventure.getPictures());
        return new ResponseEntity<>(adventureDTO, HttpStatus.OK);
    }
}
