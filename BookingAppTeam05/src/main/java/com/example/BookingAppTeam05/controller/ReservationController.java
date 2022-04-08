package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
}
