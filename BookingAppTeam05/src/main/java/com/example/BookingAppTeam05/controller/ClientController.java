package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ClientController {
    @Autowired
    private ClientService clientService;
}
