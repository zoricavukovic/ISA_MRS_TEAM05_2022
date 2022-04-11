package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Adventure;
import com.example.BookingAppTeam05.repository.AdventureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdventureService {
    private AdventureRepository adventureRepository;

    @Autowired
    public AdventureService(AdventureRepository adventureRepository) {
        this.adventureRepository = adventureRepository;
    }

    public Adventure getAdventureById(Long id) {
        return this.adventureRepository.getAdventureById(id);
    }
}
