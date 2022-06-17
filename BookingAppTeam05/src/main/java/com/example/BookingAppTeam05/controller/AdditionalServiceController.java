package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.NewAdditionalServiceDTO;
import com.example.BookingAppTeam05.service.AdditionalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<NewAdditionalServiceDTO>> getAdditionalServicesByReservationId(@PathVariable Long resId) {
        List<NewAdditionalServiceDTO> additionalServicesDTOs = additionalServiceService.findAdditionalServicesDTOByReservationId(resId);
        return new ResponseEntity<>(additionalServicesDTOs, HttpStatus.OK);
    }
}
