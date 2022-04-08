package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {
    @Autowired
    private PlaceRepository placeRepository;
}
