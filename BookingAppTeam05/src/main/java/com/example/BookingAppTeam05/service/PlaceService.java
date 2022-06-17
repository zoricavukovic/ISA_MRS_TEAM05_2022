package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.PlaceDTO;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    private PlaceRepository placeRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository){
        this.placeRepository = placeRepository;
    }

    public PlaceService(){}

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    public Place getPlaceById(Long id) {
        Optional<Place> placeResult =  placeRepository.findById(id);
        return placeResult.orElse(null);
    }

    public Place getPlaceByZipCode(String zipCode) {
        return placeRepository.getPlaceByZipCode(zipCode);  }

    public List<PlaceDTO> findAllPlaceDTO() {
        List<Place> places = findAll();
        List<PlaceDTO> placeDTOS = new ArrayList<>();
        for (Place place : places){
            PlaceDTO placeDTO = new PlaceDTO(place);
            placeDTOS.add(placeDTO);
        }
        return placeDTOS;
    }
}
