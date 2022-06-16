package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.systemRevenue.SystemRevenueForPeriodDTO;
import com.example.BookingAppTeam05.dto.systemRevenue.SystemRevenuePercentageDTO;
import com.example.BookingAppTeam05.service.SystemRevenuePercentageService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/systemRevenuePercentages")
public class SystemRevenuePercentageController {

    private SystemRevenuePercentageService systemRevenuePercentageService;

    public SystemRevenuePercentageController(SystemRevenuePercentageService systemRevenuePercentageService) {
        this.systemRevenuePercentageService = systemRevenuePercentageService;
    }

    public SystemRevenuePercentageController(){}

    @GetMapping(value="/current", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<SystemRevenuePercentageDTO> getCurrentSystemRevenuePercentage() {
        SystemRevenuePercentageDTO retVal = systemRevenuePercentageService.getCurrentSystemRevenuePercentageDTO();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SystemRevenuePercentageDTO> setNewLoyaltyProgram(@Valid @RequestBody SystemRevenuePercentageDTO systemRevenuePercentageDTO) {
        SystemRevenuePercentageDTO created = systemRevenuePercentageService.setNewSystemRevenuePercentageDTO(systemRevenuePercentageDTO);
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    @GetMapping(value="/revenueInPeriod")
    public ResponseEntity<SystemRevenueForPeriodDTO> getRevenueForPeriod(@RequestParam("startDate") @DateTimeFormat(pattern="yyyy-dd-MM") LocalDate startDate , @RequestParam("endDate") @DateTimeFormat(pattern="yyyy-dd-MM") LocalDate endDate) {
        SystemRevenueForPeriodDTO retVal = systemRevenuePercentageService.getSystemRevenueDTOForPeriod(startDate, endDate);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @GetMapping(value="/allRevenue")
    public ResponseEntity<SystemRevenueForPeriodDTO> getAllRevenue() {
        SystemRevenueForPeriodDTO retVal = systemRevenuePercentageService.getSystemRevenueDTOForAll();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }
}
