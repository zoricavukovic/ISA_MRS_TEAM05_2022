package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.CottageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CottageController {
    @Autowired
    private CottageService cottageService;
}
