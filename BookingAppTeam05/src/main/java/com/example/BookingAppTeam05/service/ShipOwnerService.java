package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.ShipOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipOwnerService {
    @Autowired
    private ShipOwnerRepository shipOwnerRepository;
}
