package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.model.CottageOwner;
import com.example.BookingAppTeam05.repository.CottageOwnerRepository;
import com.example.BookingAppTeam05.repository.CottageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
public class CottageOwnerService {
    private CottageOwnerRepository cottageOwnerRepository;
    private CottageRepository cottageRepository;
    @Autowired
    private CottageOwnerService(CottageOwnerRepository cottageOwnerRepository, CottageRepository cottageRepository){
        this.cottageOwnerRepository = cottageOwnerRepository;
        this.cottageRepository = cottageRepository;
    }
}
