package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.UnavailableDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnavailableDateService {
    @Autowired
    private UnavailableDateRepository unavailableDateRepository;
}
