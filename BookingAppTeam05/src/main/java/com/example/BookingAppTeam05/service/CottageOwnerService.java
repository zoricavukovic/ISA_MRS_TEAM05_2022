package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.CottageOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CottageOwnerService {
    @Autowired
    private CottageOwnerRepository cottageOwnerRepository;
}
