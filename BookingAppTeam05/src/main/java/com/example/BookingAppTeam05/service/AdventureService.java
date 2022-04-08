package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.AdventureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdventureService {
    @Autowired
    private AdventureRepository adventureRepository;
}
