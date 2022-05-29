package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.systemRevenue.SystemRevenueForPeriodDTO;
import com.example.BookingAppTeam05.dto.systemRevenue.SystemRevenuePercentageDTO;
import com.example.BookingAppTeam05.model.SystemRevenuePercentage;
import com.example.BookingAppTeam05.service.SystemRevenuePercentageService;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

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

    @GetMapping(value="/revenueInPeriod")
    public ResponseEntity<SystemRevenueForPeriodDTO> getRevenueForPeriod(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        //SystemRevenueForPeriodDTO s = systemRevenuePercentageService.getSystemRevenueDTOForPeriod(startDate, endDate);
        SystemRevenueForPeriodDTO s = new SystemRevenueForPeriodDTO(50000.0, 30000.0, 2000.0, 5000.0, 10.0, 11.0, 50.0);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @GetMapping(value="/allRevenue")
    public ResponseEntity<SystemRevenueForPeriodDTO> getAllRevenue() {
        SystemRevenueForPeriodDTO s = new SystemRevenueForPeriodDTO(10000.0, 3000.0, 2000.0, 5000.0, 10.0, 11.0, 50.0);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }


}
