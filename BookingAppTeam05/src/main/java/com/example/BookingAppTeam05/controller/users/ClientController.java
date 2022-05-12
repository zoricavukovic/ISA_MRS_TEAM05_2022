package com.example.BookingAppTeam05.controller.users;

import com.example.BookingAppTeam05.service.users.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ClientController {
    @Autowired
    private ClientService clientService;
}
