package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.PlaceDTO;
import com.example.BookingAppTeam05.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {

    private PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    public PlaceController(){}

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlaces(){
        List<PlaceDTO> placeDTOs = placeService.findAllPlaceDTO();
        return new ResponseEntity<>(placeDTOs, HttpStatus.OK);
    }
}
