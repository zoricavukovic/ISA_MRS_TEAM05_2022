package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.repository.users.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
}
