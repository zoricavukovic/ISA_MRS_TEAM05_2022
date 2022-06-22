package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.NewAdditionalServiceDTO;
import com.example.BookingAppTeam05.service.AdditionalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/additionalServices")
public class AdditionalServiceController {
    private AdditionalServiceService additionalServiceService;

    @Autowired
    public AdditionalServiceController(AdditionalServiceService additionalServiceService){
        this.additionalServiceService = additionalServiceService;
    }

    public AdditionalServiceController(){}

    @GetMapping(value="/{resId}")
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER', 'ROLE_INSTRUCTOR', 'ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<NewAdditionalServiceDTO>> getAdditionalServicesByReservationId(@PathVariable Long resId) {
        List<NewAdditionalServiceDTO> additionalServicesDTOs = additionalServiceService.findAdditionalServicesDTOByReservationId(resId);
        return new ResponseEntity<>(additionalServicesDTOs, HttpStatus.OK);
    }
}
