package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    private PlaceRepository placeRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository){
        this.placeRepository = placeRepository;

    }

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    public Place getPlaceById(Long id) {
        Optional<Place> placeResult =  placeRepository.findById(id);
        return placeResult.orElse(null);
    }

    public Place getPlaceByZipCode(String zipCode) {
        return placeRepository.getPlaceByZipCode(zipCode);  }
}
