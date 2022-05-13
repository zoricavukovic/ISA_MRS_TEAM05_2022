package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.repository.users.ShipOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipOwnerService {
    @Autowired
    private ShipOwnerRepository shipOwnerRepository;
}
