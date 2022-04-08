package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PlaceController {
    @Autowired
    private PlaceService placeService;
}
