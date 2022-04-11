package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Place getPlaceById(Long id) {return placeRepository.getById(id); }

    public Place getPlaceByZipCode(String zipCode) { return placeRepository.getPlaceByZipCode(zipCode);  }
}
