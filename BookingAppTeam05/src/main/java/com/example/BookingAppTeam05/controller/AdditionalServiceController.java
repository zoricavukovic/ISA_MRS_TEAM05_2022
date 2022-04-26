package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.ClientDTO;
import com.example.BookingAppTeam05.dto.NewAdditionalServiceDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.service.AdditionalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/additionalServices")
public class AdditionalServiceController {
    private AdditionalServiceService additionalServiceService;

    @Autowired
    public AdditionalServiceController(AdditionalServiceService additionalServiceService){
        this.additionalServiceService = additionalServiceService;
    }

    @GetMapping(value="/{resId}")
    public ResponseEntity<List<NewAdditionalServiceDTO>> getAdditionalServicesByReservationId(@PathVariable Long resId) {
        List<AdditionalService> additionalServicesFound = additionalServiceService.findAdditionalServicesByReservationId(resId);
        List<NewAdditionalServiceDTO> additionalServicesDTOs = new ArrayList<>();
        System.out.println("Pronadjenih " + additionalServicesFound.size());
        for (AdditionalService additionalService:additionalServicesFound) {
            System.out.println("Servis " + additionalService.getServiceName() + " " + additionalService.getId());
            NewAdditionalServiceDTO aDTO = new NewAdditionalServiceDTO(additionalService.getId(), additionalService.getServiceName(), additionalService.getPrice());
            additionalServicesDTOs.add(aDTO);
        }

        return ResponseEntity.ok(additionalServicesDTOs);
    }
}
