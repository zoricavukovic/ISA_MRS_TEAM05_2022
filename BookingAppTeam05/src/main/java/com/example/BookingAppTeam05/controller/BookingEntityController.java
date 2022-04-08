package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BookingEntityController {
    @Autowired
    private BookingEntityService bookingEntityService;
}
