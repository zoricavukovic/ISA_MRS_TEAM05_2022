package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.UnavailableDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UnavailableDateController {
    @Autowired
    private UnavailableDateService unavailableDateService;
}
