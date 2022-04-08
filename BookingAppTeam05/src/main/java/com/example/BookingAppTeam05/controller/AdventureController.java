package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.AdventureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AdventureController {
    @Autowired
    private AdventureService adventureService;
}
