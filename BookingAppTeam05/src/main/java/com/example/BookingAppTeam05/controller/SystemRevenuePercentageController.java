package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.LoyaltyProgramDTO;
import com.example.BookingAppTeam05.dto.SystemRevenuePercentageDTO;
import com.example.BookingAppTeam05.model.SystemRevenuePercentage;
import com.example.BookingAppTeam05.service.SystemRevenuePercentageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/systemRevenuePercentages")
public class SystemRevenuePercentageController {

    private SystemRevenuePercentageService systemRevenuePercentageService;

    public SystemRevenuePercentageController(SystemRevenuePercentageService systemRevenuePercentageService) {
        this.systemRevenuePercentageService = systemRevenuePercentageService;
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @GetMapping(value="/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SystemRevenuePercentageDTO> getCurrentSystemRevenuePercentage() {
        SystemRevenuePercentage s = systemRevenuePercentageService.getCurrentSystemRevenuePercentage();
        SystemRevenuePercentageDTO retVal = new SystemRevenuePercentageDTO(s);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SystemRevenuePercentageDTO> setNewLoyaltyProgram(@Valid @RequestBody SystemRevenuePercentageDTO systemRevenuePercentageDTO) {
        SystemRevenuePercentageDTO created = systemRevenuePercentageService.setNewSystemRevenuePercentageDTO(systemRevenuePercentageDTO);
        if (created == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(created, HttpStatus.OK);
    }
}
