package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.model.User;
import com.example.BookingAppTeam05.repository.CottageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CottageService {
    private CottageRepository cottageRepository;

    @Autowired
    public CottageService(CottageRepository cottageRepository) {
        this.cottageRepository = cottageRepository;
    }

    public Cottage getCottageById(Long id) {
        return cottageRepository.getCottageById(id);
    }

    public List<Cottage> findAll() { return cottageRepository.findAll();    }
}
