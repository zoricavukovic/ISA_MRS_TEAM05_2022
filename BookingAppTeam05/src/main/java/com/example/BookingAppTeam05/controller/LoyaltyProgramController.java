package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.LoyaltyProgramDTO;
import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.service.LoyaltyProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/loyaltyPrograms")
public class LoyaltyProgramController {

    private LoyaltyProgramService loyaltyProgramService;

    @Autowired
    public LoyaltyProgramController(LoyaltyProgramService loyaltyProgramService) {
        this.loyaltyProgramService = loyaltyProgramService;
    }

    @GetMapping(value="/current", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoyaltyProgramDTO> getCurrentLoyaltyProgram() {
        LoyaltyProgram loyaltyProgram = loyaltyProgramService.getCurrentLoyaltyProgram();
        LoyaltyProgramDTO loyaltyProgramDTO = new LoyaltyProgramDTO(loyaltyProgram);
        return new ResponseEntity<>(loyaltyProgramDTO, HttpStatus.OK);
    }

    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoyaltyProgramDTO> setNewLoyaltyProgram(@Valid @RequestBody LoyaltyProgramDTO loyaltyProgramDTO) {
        LoyaltyProgramDTO created = loyaltyProgramService.createNewLoyaltyProgram(loyaltyProgramDTO);
        System.out.println("usaoooo");
        if (created == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

}
